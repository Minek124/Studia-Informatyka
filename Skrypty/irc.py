#!/usr/bin/env python
# -*- coding: utf-8 -*-
# PJS1 MICHAL MOGILA - SKRYPT ZALICZENIOWY - PYTHON - IRC
import socket              
import sys
import fcntl, os
import errno
import threading
import Queue
import signal

def handler(signum, frame):
    print 'Ctrl+Z nie działa, zamykać proszę Ctrl-C'
	
def handler2(signum, frame):
	print "Skoro prosisz to schodzę"
	sys.exit(1)

signal.signal(signal.SIGTSTP, handler)
signal.signal(signal.SIGINT, handler2)

def add_input(input_queue):
	while True:
		try:
			input = raw_input()
			input_queue.put(input)
			print "\033[A                             \033[A"
		except EOFError:
			pass
		

class Client:
	def __init__(self, sock, name):
		self.s = sock
		self.name = name
		self.power = 0
		self.type = 0;
	def recv(self):
		return self.s.recv(1024)
	def send(self, msg):
		self.s.send(msg)
	def close(self):
		self.s.close()
		
ipArg = False
portArg = False;
client = False
server = False
port = 12345
ip = "127.0.0.1"

for var in sys.argv[1:]:
	if  var == "-h"  or  var == "--help" :
		print "PROJEKT ZALICZENIOWY - IRC"
		print "URUCHAMIANIE: irc.py [OPCJE]"
		print "OPCJE:"
		print "--help -h  		wyświetla pomoc"
		print "-s			wymusza uruchomienia skryptu jako server, nawet gdy skrypt nazywa się client"
		print "-c			wymusza uruchomienia skryptu jako client, nawet gdy skrypt nazywa się server"
		print "-a <ADRES IP>		ustawia preferowany adres ip (domyślnie localhost)"
		print "-a <port>		ustawia preferowany adres port (domyślnie 12345)"
		print "przykładowe użycie: "
		print "1. Uruchomić Server: python server.py lub python irc.py -s lub python server -a 127.0.0.1 -p 9999 "
		print "Pracę serwera kończymy Ctrl-C "
		print "1. Uruchomić Klienta: python klient.py lub python irc.py -c lub python client -a 127.0.0.1 -p 9999 "
		print "po połączeniu z serwerem klient na możliwość wywołania danych komend:"
		print "wszystkie wiadomości nie zaczynające się od / są wysyłane do wszystkich klientow serwera"
		print "/kick Name - wyrzuca klienta o nazwie Name"
		print "/mute Name - odbiera prawo pisania w czacie klientowi o nazwie Name"
		print "/unmute Name - przywraca prawo pisania w czacie klientowi o nazwie Name"
		print "/halfop Name - daje uprawnienia poziomu 1 klientowi o nazwie Name, te uprawnienia dają możliwość komend /kick /mute /unmute"
		print "/op Name - daje uprawnienia poziomu 2 klientowi o nazwie Name, te uprawnienia dają możliwość komend /kick /mute /unmute /halfop /op"
		print "/name Name - zmienia nazwę klienta na Name"
		print "/exit - wychodzenie z czatu"
		print "uprawnienia najwyższego poziomu (power 999) dostaje osoba która pierwsza połączyła się z serwerem"		
		print "autor Michał Mogiła"
		sys.exit(1)
	if ipArg :
		ip = var
		ipArg = False
	elif portArg :
		port = int(var)
		portArg = False
	elif var == "-a" :
		ipArg = True
	elif var == "-p" :
		portArg = True
	elif var == "-s" :
		server = True
	elif var == "-c" :
		client = True
if "client" in sys.argv[0] or "klient" in sys.argv[0] or client:
	print "Running Client"
	s = socket.socket()                  
	print "Connecting to:",ip,":",port, " ..."
	try:
		s.connect((ip, port))
	except socket.error, e:
		print "can't connect to the server, make sure it is running or wrong port or ip address"
		print e
		sys.exit(1)
	print "Connected!"
	s.setblocking(0)
	input_queue = Queue.Queue()
	input_thread = threading.Thread(target=add_input, args=(input_queue,))
	input_thread.daemon = True
	input_thread.start()
	while True:
		if not input_queue.empty():
			x = input_queue.get()
			s.send(x)
		try:
			msg = s.recv(1024)
			if not msg: 
				print "Connection lost (server shutdown or kicked)"
				sys.exit(1)
			sys.stdout.write(msg)
		except socket.error, e:
			err = e.args[0]
			if err == errno.EAGAIN or err == errno.EWOULDBLOCK:
				pass
			elif err == 113:
				print "Connection lost (server shutdown or kicked)"
				s.close()
				sys.exit(1)
			else:
				print e
				sys.exit(1)
elif "server" in sys.argv[0] or "serwer" in sys.argv[0] or server:
	print "Running Serwer on:", ip,":",port
	cl = []
	
	ss = socket.socket()       
	ss.bind((ip, port))      
	ss.setblocking(0)
	ss.listen(5)
	print "Server is waiting for clients..."
	it = 0

	while True:
		try:
			sock, addr = ss.accept()
			c = Client(sock,"anonymous" + str(it))
			c.send("SERVER:" + "Connected!" + '\n')
			if it == 0: 
				c.power = 999
				c.send("SERVER:" + "You are owner here" + '\n')
			it += 1
			c.s.setblocking(0)
			for s2 in cl:
				s2.send("SERVER:" + c.name + " has Connected!" + '\n')
			cl.append(c)
			print 'Got connection from', addr
		except socket.error, e:
			pass
		for s in cl:
			try:
				msg = s.recv()
				#print msg
				if msg.startswith("/name "):
					print "/name command executed by: " + s.name + " value: " + msg[6:]
					s.name = msg[6:]
					s.send("SERVER:" + "Your name is " + s.name + " now" + '\n')
				elif msg.startswith("/perlClient"):
					print s.name + " is now perl Client" + '\n';
					s.type = 1
				elif msg.startswith("/kick "):
					print "/kick command executed by: " + s.name + " value: " + msg[6:]
					if s.power >= 1:
						found = False
						for s2 in cl:
							if s2.name == msg[6:]:
								if s.power >= s2.power :
									s2.send("SERVER:" + "You have been kicked! " + '\n')
									if s2.type == 1 :
										s2.send("PerlForceKickHack")
									print s2.name + " has disconnected! "
									s2.close()
									cl.remove(s2)
									for s3 in cl:
										s3.send(s2.name + " has been kicked!" + '\n')
								else:
									s.send("SERVER:" + "you have less privileges than " + s2.name + "! Your power is: " + str(s.power) + " ," + s2.name + " power is:" + str(s2.power) + '\n')
								found = True
						if not found:
							s.send("SERVER:" + "Name:" + msg[6:] + " not found!" + '\n')
					else:
						s.send("SERVER:" + "permission denied! Your power is: " + str(s.power) + '\n')
				elif msg.startswith("/exit"):
					print "/exit command executed by: " + s.name
					if s.type == 1 :
						s.send("PerlForceKickHack")
					print s.name + " has disconnected! "
					s.close()
					cl.remove(s)
				elif msg.startswith("/mute "):
					print "/mute command executed by: " + s.name + " value: " + msg[6:]
					if s.power >= 1:
						found = False
						for s2 in cl:
							if s2.name == msg[6:]:
								if s.power >= s2.power :
									s2.power = -1
									for s3 in cl:
										s3.send(s2.name + " has been muted!" + '\n')
								else:
									s.send("SERVER:" + "you have less privileges than " + s2.name + "! Your power is: " + str(s.power) + " ," + s2.name + " power is:" + str(s2.power) + '\n')
								found = True
						if not found:
							s.send("SERVER:" + "Name:" + msg[6:] + " not found!" + '\n')
					else:
						s.send("SERVER:" + "permission denied! Your power is: " + str(s.power) + '\n')
				elif msg.startswith("/unmute "):
					print "/unmute command executed by: " + s.name + " value: " + msg[8:]
					if s.power >= 1:
						found = False
						for s2 in cl:
							if s2.name == msg[8:]:
								if s.power >= s2.power :
									s2.power = 0
									for s3 in cl:
										s3.send(s2.name + " has been unmuted!" + '\n')
								else:
									s.send("SERVER:" + "you have less privileges than " + s2.name + "! Your power is: " + str(s.power) + " ," + s2.name + " power is:" + str(s2.power) + '\n')
								found = True
						if not found:
							s.send("SERVER:" + "Name:" + msg[8:] + " not found!" + '\n')
					else:
						s.send("SERVER:" + "permission denied! Your power is: " + str(s.power) + '\n')
				elif msg.startswith("/op "):
					print "/op command executed by: " + s.name + " value: " + msg[4:]
					if s.power > 10:
						found = False
						for s2 in cl:
							if s2.name == msg[4:]:
								if s.power >= s2.power :
									s2.power = 2
									for s3 in cl:
										s3.send(s2.name + " is op now!" + '\n')
								else:
									s.send("SERVER:" + "you have less privileges than " + s2.name + "! Your power is: " + str(s.power) + " ," + s2.name + " power is:" + str(s2.power) + '\n')
								found = True
						if not found:
							s.send("SERVER:" + "Name:" + msg[4:] + " not found!" + '\n')
					else:
						s.send("SERVER:" + "permission denied! Your power is: " + str(s.power) + '\n')
				elif msg.startswith("/halfop "):
					print "/halfop command executed by: " + s.name + " value: " + msg[8:]
					if s.power >= 2:
						found = False
						for s2 in cl:
							if s2.name == msg[8:]:
								if s.power >= s2.power :
									s2.power = 1
									for s3 in cl:
										s3.send(s2.name + " is halfop now!" + '\n')
								else:
									s.send("SERVER:" + "you have less privileges than " + s2.name + "! Your power is: " + str(s.power) + " ," + s2.name + " power is:" + str(s2.power) + '\n')
								found = True
						if not found:
							s.send("SERVER:" + "Name:" + msg[8:] + " not found!" + '\n')
					else:
						s.send("SERVER:" + "permission denied! Your power is: " + str(s.power) + '\n')
				else:
					if not msg.startswith("/"):
						if s.power >= 0:
							for s2 in cl:
								s2.send(s.name + "> " + msg + '\n')
						else:
							s.send("SERVER:" + "you are muted!" + '\n')
					else:
						s.send("SERVER:" + "Unknown command!" + '\n')
			except socket.error, e:
				err = e.args[0]
				if err == errno.EAGAIN or err == errno.EWOULDBLOCK:
					pass
				else:
					print s.name + " has disconnected! "
					s.close()
					cl.remove(s)

print "użyj opcji --help lub zmień nazwę pliku na server lub client"