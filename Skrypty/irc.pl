#!/usr/bin/env perl
# PJS1 MICHAL MOGILA - SKRYPT ZALICZENIOWY - PERL - IRC

use IO::Socket::INET;
use IO::Select;
use Client;
$ipArg = 0;
$portArg = 0;
$client = 0;
$server = 0;
$port = 12345;
$ip = "127.0.0.1";

foreach (@ARGV) {
	if  ( $_ eq "-h"  or  $_ eq "--help" ){
		print "PROJEKT ZALICZENIOWY - IRC\n";
		print "URUCHAMIANIE: irc.pl [OPCJE]\n";
		print "OPCJE:\n";
		print "--help -h  		wyswietla pomoc\n";
		print "-s			wymusza uruchomienia skryptu jako server, nawet gdy skrypt nazywa sie client\n";
		print "-c			wymusza uruchomienia skryptu jako client, nawet gdy skrypt nazywa sie server\n";
		print "-a <ADRES IP>		ustawia preferowany adres ip (domyslnie localhost)\n";
		print "-a <port>		ustawia preferowany adres port (domyslnie 12345)\n";
		print "przykladowe uzycie: \n";
		print "1. Uruchomic Server: perl server.pl lub ./irc.pl -s lub perl server -a 127.0.0.1 -p 9999 \n";
		print "Pracę serwera kończymy Ctrl-C \n";
		print "1. Uruchomic Klienta: perl klient.pl lub perl irc.pl -c lub perl client -a 127.0.0.1 -p 9999 \n";
		print "po polaczeniu z serwerem klient na mozliwosc wywolania danych komend:\n";
		print "wszystkie wiadomosci nie zaczynajace sie od / sa wysylane do wszystkich klientow serwera\n";
		print "/kick Name - wyrzuca klienta o nazwie Name\n";
		print "/mute Name - odbiera prawo pisania w czacie klientowi o nazwie Name\n";
		print "/unmute Name - przywraca prawo pisania w czacie klientowi o nazwie Name\n";
		print "/halfop Name - daje uprawnienia poziomu 1 klientowi o nazwie Name, te uprawnienia daja mozliwosc komend /kick /mute /unmute\n";
		print "/op Name - daje uprawnienia poziomu 2 klientowi o nazwie Name, te uprawnienia daja mozliwosc komend /kick /mute /unmute /halfop /op\n";
		print "/name Name - zmienia nazwe klienta na Name\n";
		print "/exit - wychodzenie z czatu\n";
		print "uprawnienia najwyzszego poziomu (power 999) dostaje osoba ktora pierwsza polaczyla sie z serwerem\n";
		print "autor Michal Mogila\n";
		exit;
	}
	if ( $ipArg ){
		$ip = $_;
		$ipArg = 0;
	}elsif ( $portArg ){
		$port = $_; # parse int
		$portArg = 0;
	}elsif ( $_ eq "-a" ){
		$ipArg = 1;
	}elsif ( $_ eq "-p" ){
		$portArg = 1;
	}elsif ( $_ eq "-s" ){
		$server = 1;
	}elsif ( $_ eq "-c" ){
		$client = 1;
	}
}
if ( $0 =~ /client/ or $0 =~ /klient/ or $client ){	
	print "Running Client\n";

	print "Connecting to: $ip:$port  ...\n";
	# auto-flush on socket
	$| = 1;
 
	$in = IO::Select->new();
	$in->add(\*STDIN);
	my $socket = new IO::Socket::INET (
		PeerHost => $ip,
		PeerPort => $port,
		Proto => 'tcp',
		Blocking => 0,
	);
	die "Could not create socket, info: $!\n" unless $socket;
	
	print "Socket created!\n";
	$socket->send("/perlClient");
	$timer = time();
	
	
	while ( 1 ){
		$socket->recv($msg, 1024);
		if ( $timer != 0 && time()-$timer > 3){
			print "can't connect to the server, make sure it is running or wrong port or ip address";
			exit 2;
		}
		if ($msg){ 
			if ( $msg eq "PerlForceKickHack" ){
				$socket->close();
				exit 2;
			}
			print $msg;
			$timer = 0;
		}
		if ($in->can_read(0)){
			chomp($x = <STDIN>);
			print "\033[A\033[A";
			
			print "\033[1B";     
		}
		if ( $x ){
			$socket->send($x);
			$x = "";
		}
	}
	$socket->close();
}elsif ($0 =~ /server/ or $0 =~ /serwer/ or $server ){
	print "Running Serwer on: $ip:$port \n";
	my @cl = ();
	$| = 1;
 
	
	my $ss = new IO::Socket::INET (
		LocalHost => $ip,
		LocalPort => $port,
		Proto => 'tcp',
		Listen => 5,
		Reuse => 1,
		Blocking => 0,
	);
	die "Could not create socket, info: $!\n" unless $ss;
	
	#$sel = IO::Select->new( $ss );
	$sel = IO::Select->new();
	$sel->add($ss);
	print "Socket created!\n";
	
	#$timer = time();
	print "Server is waiting for clients...\n";
	$it = 0;
	

	while ( 1 ){
		if ($sel->can_read(0)){
			my $s = $ss->accept();
			
			my $sel2 = IO::Select->new();
			$sel2->add($s);
			my $c = Client->new({
					s => $s,
					name => "anonymous$it",
					sel => $sel2,
				});
			$c->send("SERVER:Connected!\n");
			if ( $it == 0 ){
				$c->{power} = 999;
				$c->send("SERVER:You are owner here!  \n");
			}
			$it += 1;
			foreach (@cl){
				$_->send("SERVER:$c->{name} has Connected! \n");
			}
			push (@cl, $c);
			my $addr = $s->peerhost();
			print "Got connection from: $addr \n";
		}
		foreach (@cl){
			my $s = $_;
			if ($s->{sel}->can_read(0)){
			my $msg = $s->recv();
			if ( not $msg ){
				next;
			}
			#print "$msg \n";
			my @words = split(" ", $msg);
			
			
			if ($words[0] eq "/name"){
					print "/name command executed by: $s->{name} value:$words[1] \n";
					$s->{name} = $words[1];
					$s->send("SERVER:Your name is $s->{name} now \n");
			}elsif ($words[0] eq "/perlClient"){
				print "$s->{name} is now perl Client \n";
				$s->{type} = 1;
			}elsif ($words[0] eq "/kick"){
				print "/kick command executed by:$s->{name} value:$words[1] \n";
				if ($s->{power} >= 1){
					my $found = 0;
					foreach (@cl){
						my $s2 = $_;
						if ($s2->{name} eq $words[1] ){
							if ($s->{power} >= $s2->{power}){
								$s2->send("SERVER:You have been kicked! \n");
								if ($s2->{type} == 1){
									$s2->send("PerlForceKickHack");
								}
								print "$s2->{name} has disconnected! ";
								$s2->close();
								my $index = 0;
								$index++ until $cl[$index] == $s2;
								splice(@cl, $index, 1);
								foreach (@cl){
									$_->send("$words[1] has been kicked! \n");
								}
							}else{
								$s->send("SERVER:you have less privileges than $s2->{name} , Your power is: $s->{power} , $s2->{name} power is: $s2->{power} \n");
							}
							$found = 1;
						}
					}
					if (not $found){
						$s->send("SERVER: Name: $words[1] not found \n");
					}
				}else{
						$s->send("SERVER:permission denied! Your power is: $s->{power} \n");
				}
			}elsif ($words[0] eq "/exit"){
				print "/exit command executed by: $s->{name} \n";
				if ($s->{type} == 1){
					$s->send("PerlForceKickHack");
				}
				print "$s->{name} has disconnected! ";
				$s->close();
				my $index = 0;
				$index++ until $cl[$index] == $s;
				splice(@cl, $index, 1);
			}elsif ($words[0] eq "/mute"){
				print "/mute command executed by: $s->{name} value: $words[1] \n";
				if ($s->{power} >= 1){
					$found = 0;
					foreach (@cl){
						my $s2 = $_;
						if ($s2->{name} eq $words[1]){
							if ($s->{power} >= $s2->{power}){
								$s2->{power} = -1;
								$s2->send("SERVER:you are muted now! \n");
								foreach (@cl){
									$_->send("$words[1] has been muted! \n");
								}
							}else{
								$s->send("SERVER:you have less privileges than $s2->{name} , Your power is: $s->{power} , $s2->{name} power is: $s2->{power} \n");
							}
							$found = 1;
						}
					}
					if (not $found){
						$s->send("SERVER:Name: $words[1] not found \n");
					}
				}else{
						$s->send("SERVER:permission denied! Your power is: $s->{power} \n");
				}
			}elsif ($words[0] eq "/unmute"){
				print "/unmute command executed by: $s->{name} value: $words[1]  \n";
				if ($s->{power} >= 1){
					$found = 0;
					foreach (@cl){
						my $s2 = $_;
						if ($s2->{name} eq $words[1]){
							if ($s->{power} >= $s2->{power}){
								$s2->{power} = 0;
								$s2->send("SERVER:you are unmuted now! \n");
								foreach (@cl){
									$_->send("$words[1] has been unmuted! \n");
								}
							}else{
								$s->send("SERVER:you have less privileges than $s2->{name} , Your power is: $s->{power} , $s2->{name} power is: $s2->{power} \n");
							}
							$found = 1;
						}
					}
					if (not $found){
						$s->send("SERVER:Name: $words[1] not found \n");
					}
				}else{
						$s->send("SERVER:permission denied! Your power is: $s->{power} \n");
				}
			}elsif ($words[0] eq "/op"){
				print "/op command executed by: $s->{name} value: $words[1] \n";
				if ($s->{power} >= 10){
					$found = 0;
					foreach (@cl){
						my $s2 = $_;
						if ($s2->{name} eq $words[1]){
							if ($s->{power} >= $s2->{power}){
								$s2->{power} = 2;
								foreach (@cl){
									$_->send("$words[1] is op now! \n");
								}
							}else{
								$s->send("SERVER:you have less privileges than $s2->{name} , Your power is: $s->{power} , $s2->{name} power is: $s2->{power} \n");
							}
							$found = 1;
						}
					}
					if (not $found){
						$s->send("SERVER:Name: $words[1] not found \n");
					}
				}else{
						$s->send("SERVER:permission denied! Your power is: $s->{power} \n");
				}
			}elsif ($words[0] eq "/halfop"){
				print "/halfop command executed by: $s->{name} value: $words[1] \n";
				if ($s->{power} >= 2){
					$found = 0;
					foreach (@cl){
						my $s2 = $_;
						if ($s2->{name} eq $words[1]){
							if ($s->{power} >= $s2->{power}){
								$s2->{power} = 1;
								foreach (@cl){
									$_->send("$words[1] is halfop now! \n");
								}
							}else{
								$s->send("SERVER:you have less privileges than $s2->{name} , Your power is: $s->{power} , $s2->{name} power is: $s2->{power} \n");
							}
							$found = 1;
						}
					}
					if (not $found){
						$s->send("SERVER:Name: $words[1] not found \n");
					}
				}else{
						$s->send("SERVER:permission denied! Your power is: $s->{power} \n");
				}
			}else{
				my @chars = split("", $msg);
				if (not ($chars[0] eq "/")){
					if ($s->{power} >= 0){
						foreach (@cl){
							$_->send("$s->{name}>$msg \n");
						}
					}else{
						$s->send("SERVER: you are muted! \n");
					}
				}else{
					$s->send("SERVER: Unknown command! \n");
				}
			}
			}
		}
	}
}
print "uzyj opcji --help lub zmien nazwe pliku na server lub client \n";
