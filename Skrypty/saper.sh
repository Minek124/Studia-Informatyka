#!/bin/bash
# PJS1 MICHAL MOGILA - SKRYPT ZALICZENIOWY - BASH

declare -a bombs
declare -a revealed
declare -a count
declare -i size=10
declare -i lastIndex=$((size-1))
declare -i difficulty=10
declare -i bombAmount=10
declare -i cursorX=0
declare -i cursorY=0
declare -i state=0
declare -i revealedAmount=0
declare -i startTime

for var in "$@"
do
    if [ "$var" == "-h" ] || [ "$var" == "--help" ]; then
		echo "PROJEKT ZALICZENIOWY - SAPER";
		echo "URUCHAMIANIE: saper.sh [OPCJE]";
		echo "OPCJE:"
		echo "-s <LICZBA>	parametr okresla wielkosc pola minowego (domyślnie 10)"
		echo "-d <LICZBA>	parametr okresla % zapelnienia pola minami, np 10% przy wielkosci rownej 10 będzie 10 min (domyślnie 10)"
		echo "--help -h  	wyswietla pomoc";
		echo "przykładowe uzycie ./saper.sh -s 10 -d 15  , czego wynikiem bedzie plansza o boku 10 i 15% zapełnieniem minami (przy domyslnej powloce bash)"
		echo "oznaczenia pól: # - nieodkryte pole , * - zaznaczone pole, brak możliwości odkrycia "
		echo "poruszanie się po planszy przy pomocy klawiszy w,s,a,d "
		echo "odsłonięcie pola przy pomocy klawiszy: e lub o"
		echo "zaznaczenie/odznaczenie miny przy pomocy klawiszy: r lub p"
		echo "zaznaczenie miny powoduje jedynie zmianę wizerunku pola oraz niemożnością przypadkowego odkrycia"
		echo "Aby wygrać trzeba odsłonić wszystkie niezaminowane pola"
		echo "autor michal mogila"
		exit 0;
	fi
	if [ "$sizeArg" == "1" ]; then
		if [[ $var =~ ^[0-9]+$ ]]; then
			size=$var
			sizeArg="0"
		else
			echo "niepoprawny parametr $var , musi być liczbą dodatnia"
			exit 1;
		fi
	fi
	if [ "$diffArg" == "1" ]; then
		if [[ $var =~ ^[0-9]+$ ]]; then
			difficulty=$var
			diffArg="0"
		else
			echo "niepoprawny parametr $var , musi być liczbą dodatnia"
			exit 1;
		fi
	fi
	if [ "$var" == "-s" ]; then
		sizeArg="1"
	fi
	if [ "$var" == "-d" ]; then
		diffArg="1"
	fi
	
done


bombAmount=$(((size*size)*difficulty/100))
lastIndex=$((size-1))
echo "wielkość planszy: $size"
echo "trudność: $difficulty %"
echo "ilość bomb: $bombAmount"

function revealNear {
	found=1
	while [ $found -eq 1 ] ; do
		found=0;
		for (( i=0; i<$size; i++ )) do
			for (( j=0; j<$size; j++ )) do
				if [ "${revealed[$(((i*size)+j))]}" == "1" ] && [ ${count[$(((i*size)+j))]} -eq 0 ]; then
					if [ "${revealed[$(((i*size)+j))]}" == "1" ]; then
						if  [ $i -ne 0 ] && [ $j -ne 0 ] && [ "${revealed[$((((i-1)*size)+j-1))]}" == "0" ]; then
							revealed[$((((i-1)*size)+j-1))]="1"
							found=1
							((revealedAmount++))
						fi
						if [ $i -ne 0 ] && [ "${revealed[$((((i-1)*size)+j))]}" == "0" ]; then
							revealed[$((((i-1)*size)+j))]="1"
							found=1
							((revealedAmount++))
						fi
						if [ $i -ne 0 ] && [ $j -ne $lastIndex ] && [ "${revealed[$((((i-1)*size)+j+1))]}" == "0" ]; then
							revealed[$((((i-1)*size)+j+1))]="1"
							found=1
							((revealedAmount++))
						fi
						if [ $j -ne 0 ] && [ "${revealed[$(((i*size)+j-1))]}" == "0" ]; then
							revealed[$((((i)*size)+j-1))]="1"
							found=1
							((revealedAmount++))
						fi
						if [ $j -ne $lastIndex ] && [ "${revealed[$(((i*size)+j+1))]}" == "0" ]; then
							revealed[$((((i)*size)+j+1))]="1"
							found=1
							((revealedAmount++))
						fi
						if [ $i -ne $lastIndex ] && [ $j -ne 0 ] && [ "${revealed[$((((i+1)*size)+j-1))]}" == "0" ]; then
							revealed[$((((i+1)*size)+j-1))]="1"
							found=1
							((revealedAmount++))
						fi
						if [ $i -ne $lastIndex ] && [ "${revealed[$((((i+1)*size)+j))]}" == "0" ]; then
							revealed[$((((i+1)*size)+j))]="1"
							found=1
							((revealedAmount++))
						fi
						if [ $i -ne $lastIndex ] && [ $j -ne $lastIndex ] && [ "${revealed[$((((i+1)*size)+j+1))]}" == "0" ]; then
							revealed[$((((i+1)*size)+j+1))]="1"
							found=1
							((revealedAmount++))
						fi
					fi
				fi
			done
		done
	done
}

function draw {
	printf "\033c"
	echo 
	for (( i=0; i<$size; i++ )) do
		for (( j=0; j<$size; j++ )) do
			if [ "$i" == "$cursorY" ] && [ "$j" == "$cursorX" ]; then
					if [ "$1" == "1" ]; then
						echo -n -e "\e[41mX\033[0m "
					else
						if [ "${revealed[$(((i*size)+j))]}" == "1" ]; then
							echo -n -e "\e[41m${count[$(((i*size)+j))]}\033[0m "
						fi
						if [ "${revealed[$(((i*size)+j))]}" == "0" ]; then
							echo -n -e "\e[41m#\033[0m "
						fi
						if [ "${revealed[$(((i*size)+j))]}" == "MARKED" ]; then
							echo -n -e "\e[41m*\033[0m "
						fi
					fi
				else
					if [ "${revealed[$(((i*size)+j))]}" == "1" ]; then
						echo -n -e "${count[$(((i*size)+j))]} "
					fi
					if [ "${revealed[$(((i*size)+j))]}" == "0" ]; then
						echo -n -e "# "
					fi
					if [ "${revealed[$(((i*size)+j))]}" == "MARKED" ]; then
						echo -n -e "* "
					fi
			fi
		done
		echo
	done
}

function clearTabs {
	for (( i=0; i<$size; i++ )) do
	for (( j=0; j<$size; j++ )) do
		bombs[$(((i*size)+j))]="0"
	done
done

for (( i=0; i<$size; i++ )) do
	for (( j=0; j<$size; j++ )) do
		revealed[$(((i*size)+j))]="0"
	done
done

}

function setUp {
state=1
c=0;
while [ $c -lt $bombAmount ] ; do
	x=$((RANDOM%size))
	y=$((RANDOM%size))
	if [ "${bombs[$(((y*size)+x))]}" != "1" ] && ([ $cursorX -ne $x ] || [ $cursorY -ne $y ]); then
		bombs[$(((y*size)+x))]="1"
		((c++))
	fi
done



for (( i=0; i<$size; i++ )) do
	for (( j=0; j<$size; j++ )) do
		summ=0;
		
		if [ $i -ne 0 ] && [ $j -ne 0 ] && [ "${bombs[$((((i-1)*size)+j-1))]}" == "1" ]; then
			((summ++))
		fi
		if [ $i -ne 0 ] && [ "${bombs[$((((i-1)*size)+j))]}" == "1" ]; then
			((summ++))
		fi
		if [ $i -ne 0 ] && [ $j -ne $lastIndex ] && [ "${bombs[$((((i-1)*size)+j+1))]}" == "1" ]; then
			((summ++))
		fi
		if [ $j -ne 0 ] && [ "${bombs[$(((i*size)+j-1))]}" == "1" ]; then
			((summ++))
		fi
		if [ $j -ne $lastIndex ] && [ "${bombs[$(((i*size)+j+1))]}" == "1" ]; then
			((summ++))
		fi
		if [ $i -ne $lastIndex ] && [ $j -ne 0 ] && [ "${bombs[$((((i+1)*size)+j-1))]}" == "1" ]; then
			((summ++))
		fi
		if [ $i -ne $lastIndex ] && [ "${bombs[$((((i+1)*size)+j))]}" == "1" ]; then
			((summ++))
		fi
		if  [ $i -ne $lastIndex ] && [ $j -ne $lastIndex ] &&[ "${bombs[$((((i+1)*size)+j+1))]}" == "1" ]; then
			((summ++))
		fi
		count[$(((i*size)+j))]=$summ
	done
done
}

function check {
	if [ $state -eq 0 ]; then 
		setUp
	fi
	if [ "${revealed[$(((cursorY*size)+cursorX))]}" != "MARKED" ]; then
		
	
	if [ "${bombs[$(((cursorY*size)+cursorX))]}" == "1" ]; then
		draw 1
		echo "Przegrales!"
		echo "Plansza o boku: $size , trudnosc: $difficulty %"
		echo "czas to: $(($(date +%s)-startTime)) sekund"
		exit 0
	else
		if [ "${revealed[$(((cursorY*size)+cursorX))]}" != "1" ]; then
			((revealedAmount++))
		fi
		revealed[$(((cursorY*size)+cursorX))]="1"
		revealNear
		
		if [ $revealedAmount -ge $(((size*size)-bombAmount)) ]; then
			draw 0
			echo "Wygrałes!"
			echo "Plansza o boku: $size , trudnosc: $difficulty %"
			echo "Osiągnięty czas to: $(($(date +%s)-startTime)) sekund"
			exit 0;
		fi
	fi
	fi
}

clearTabs
echo "Gra Saper"
echo "Poruszanie kursorem przy pomocy klawiszy w,s,a,d"
echo "odsłonięcie pola przy pomocy klawisza e lub o"
echo "zaznaczenie pola przy pomocy klawisza r lub p"
echo "Naciśnij dowolny klawisz aby zacząć..."
read -n 1
startTime=$(date +%s)

while [ $state -ne 3 ] ; do	
	
	draw 0	
	read -n 1 key
	
	case $key in
		"w")	if [ $cursorY -gt 0 ]; then
					((cursorY--))
				fi ;;
		"s")	if [ $cursorY -lt $lastIndex ]; then
					((cursorY++))
				fi ;;
		"a")	if [ $cursorX -gt 0 ]; then
					((cursorX--))
				fi ;;
		"d")	if [ $cursorX -lt $lastIndex ]; then
					((cursorX++))
				fi ;;
		"o")	check ;;
		"e")	check ;;
		"r")	if [ "${revealed[$(((cursorY*size)+cursorX))]}" == "MARKED" ]; then
					revealed[$(((cursorY*size)+cursorX))]="0"
				elif [ "${revealed[$(((cursorY*size)+cursorX))]}" == "0" ]; then
					revealed[$(((cursorY*size)+cursorX))]="MARKED"	
				fi
				;;
		"p")	if [ "${revealed[$(((cursorY*size)+cursorX))]}" == "MARKED" ]; then
					revealed[$(((cursorY*size)+cursorX))]="0"
				elif [ "${revealed[$(((cursorY*size)+cursorX))]}" == "0" ]; then
					revealed[$(((cursorY*size)+cursorX))]="MARKED"
				fi
				;;
	esac
	
done
