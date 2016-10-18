import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;

class Start {

    ArrayList<String> keyTab = new ArrayList<String>();

    void wypisz() {
        Iterator<String> it = keyTab.iterator();
        while (it.hasNext()) {
            System.out.println(it.next());
        }
    }

    boolean packages() {
        Iterator<String> it = keyTab.iterator();
        while (it.hasNext()) {
            if ("package".equals(it.next())) {
                return true;
            }
        }
        return false;
    }

    boolean publicClass() {
        Iterator<String> it = keyTab.iterator();
        boolean publicKey = false;
        while (it.hasNext()) {
            if (publicKey) {
                if ("class".equals(it.next())) {
                    return true;
                }
            }
            if ("public".equals(it.next())) {
                publicKey = true;
            } else {
                publicKey = false;
            }
        }
        return false;
    }

    boolean publicInterface() {
        Iterator<String> it = keyTab.iterator();
        boolean publicKey = false;
        while (it.hasNext()) {
            if (publicKey) {
                if ("interface".equals(it.next())) {
                    return true;
                }
            }
            if ("public".equals(it.next())) {
                publicKey = true;
            } else {
                publicKey = false;
            }
        }
        return false;
    }

    boolean startClass() {
        int braces = 0;
        Iterator<String> it = keyTab.iterator();

        while (it.hasNext()) {
            if ("Start".equals(it.next())) {
                while (it.hasNext()) {
                    if ("{".equals(it.next())) {
                        braces++;
                        while (braces > 0 && it.hasNext()) {
                            String key = it.next();
                            if ("{".equals(key)) {
                                braces++;
                            }
                            if ("}".equals(key)) {
                                braces--;
                            }
                            if ("public".equals(key)) {
                                if (it.hasNext() & "static".equals(it.next())) {
                                    if (it.hasNext() & "void".equals(it.next())) {
                                        if (it.hasNext() & "main".equals(it.next())) {
                                            return false;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return true;
    }

    String klamry() {
        Iterator<String> it = keyTab.iterator();
        LinkedList<Character> stos = new LinkedList<Character>();
        while (it.hasNext()) {
            String oldKey = it.next();
			char key=oldKey.charAt(0);
            switch (key) {
                case '{':
                    stos.add(key);
                    break;
                case '}':
                    if (stos.isEmpty()) {
                        return "blad klamr, gdzies jest samotna klamra '}'";
                    }
                    if ('{'!=stos.removeLast()) {
                        return "blad klamr, zagniezdzona klamra '}'";
                    }

                    break;
                case '(':
                    stos.add(key);
                    break;
                case ')':
                    if (stos.isEmpty()) {
                        return "blad klamr, gdzies jest samotna klamra ')'";
                    }
                    if ('('!=stos.removeLast()) {
                        return "blad klamr, zagniezdzona klamra ')'";
                    }
                    break;
            }
        }
        if (!stos.isEmpty()) {
            return "blad klamr, klamry sa niedomkniete () lub {}";
        }
        return null;
    }

    String importy() {
        Iterator<String> it = keyTab.iterator();
        boolean randomImported = false;
        boolean arrayListImported = false;
        boolean iteratorImported = false;

        while (it.hasNext()) {
            String key = it.next();
            if ("import".equals(key)) {
                String key2 = it.next();
                if ("java.util.*".equals(key2)) {
                    randomImported = true;
                    arrayListImported = true;
                    iteratorImported = true;
                }
                if ("java.util.Random".equals(key2)) {
                    randomImported = true;
                }
                if ("java.util.ArrayList".equals(key2)) {
                    arrayListImported = true;
                }
                if ("java.util.Iterator".equals(key2)) {
                    iteratorImported = true;
                }
            }
            if ("Random".equals(key)) {
                if (!randomImported) {
                    return "brak importu dla klasy Random, chyba ze tak zostala nazwana pewna klasa, mozliwe braki innych importow";
                }
            }
            if ("ArrayList".equals(key)) {
                if (!arrayListImported) {
                    return "brak importu dla klasy ArrayList, chyba ze tak zostala nazwana pewna klasa, mozliwe braki innych importow";
                }
            }
            if ("Iterator".equals(key)) {
                if (!iteratorImported) {
                    return "brak importu dla klasy Iterator, chyba ze tak zostala nazwana pewna klasa, mozliwe braki innych importow";
                }
            }
        }
        return null;
    }

    public static void main(String[] args) throws java.io.IOException {

        String file = "src.java";
        boolean sprawdzCzyStart = false;
        if (args.length >= 1) {
            file = args[0];
        }
        if (args.length == 2) {
            if ("true".equals(args[1])) {
                sprawdzCzyStart = true;
            }
        }
		java.io.FileReader in=null;
        Start sprawdzaczka = new Start();
        try {
			in=new java.io.FileReader(file);
            int c;
            String key = null;
            boolean possibleComment = false;
            while ((c = in.read()) != -1) {
                char znak = (char) c;

                switch (znak) {
                    case 32:  //spacja
                        if (key != null) {
                            sprawdzaczka.keyTab.add(key);
                            key = null;
                        }
                        possibleComment = false;
                        break;
                    case 59:  //  ";"
                        if (key != null) {
                            sprawdzaczka.keyTab.add(key);
                            key = null;
                        }
                        possibleComment = false;
                        break;
                    case 47:  //   "/"
                        if (key != null) {
                            sprawdzaczka.keyTab.add(key);
                            key = null;
                        }
                        if (possibleComment) {
                            int d;
                            while ((d = in.read()) != -1) {
                                char znak1 = (char) d;
                                if (znak1 == 10) {
                                    break;
                                }
                            }
                            possibleComment = false;
                        } else {
                            possibleComment = true;
                        }
                        break;
                    case 42:// "*"
                        if (possibleComment) {
                            int d;
                            boolean possibleEndOfComment = false;
                            while ((d = in.read()) != -1) {
                                char znak1 = (char) d;
                                if (znak1 == 47 && possibleEndOfComment) {
                                    break;
                                }

                                if (znak1 == 42) {
                                    possibleEndOfComment = true;
                                } else {
                                    possibleEndOfComment = false;
                                }
                            }
                        } else {
                            if (key == null) {
                                key = "" + znak;
                            } else {
                                key += znak;
                            }
                        }
                        possibleComment = false;
                        break;
                    case 34:// " " "
                        if (key != null) {
                            sprawdzaczka.keyTab.add(key);
                            key = null;
                        }
                        int d;
                        while ((d = in.read()) != -1) {
                            char znak1 = (char) d;
                            if(znak1==34){
                                break;
                            }
                        }
                        possibleComment = false;
                        break;
                    case 39:// " ' "
                        if (key != null) {
                            sprawdzaczka.keyTab.add(key);
                            key = null;
                        }
                        int e;
                        while ((e = in.read()) != -1) {
                            char znak1 = (char) e;
                            if(znak1==39){
                                break;
                            }
                        }
                        possibleComment = false;
                        break;
                    case 13:  //enter
                        if (key != null) {
                            sprawdzaczka.keyTab.add(key);
                            key = null;
                        }
                        possibleComment = false;
                        break;
                    case 10:  //koniec lini
                        if (key != null) {
                            sprawdzaczka.keyTab.add(key);
                            key = null;
                        }
                        possibleComment = false;
                        break;
                    case 123:  // "{"
                        if (key != null) {
                            sprawdzaczka.keyTab.add(key);
                        }
                        key = "" + znak;
                        sprawdzaczka.keyTab.add(key);
                        key = null;
                        possibleComment = false;
                        break;
                    case 125:  // "}"
                        if (key != null) {
                            sprawdzaczka.keyTab.add(key);
                        }
                        key = "" + znak;
                        sprawdzaczka.keyTab.add(key);
                        key = null;
                        possibleComment = false;
                        break;
                    case 40:  // "("
                        if (key != null) {
                            sprawdzaczka.keyTab.add(key);
                        }
                        key = "" + znak;
                        sprawdzaczka.keyTab.add(key);
                        key = null;
                        possibleComment = false;
                        break;
                    case 41:   // ")"
                        if (key != null) {
                            sprawdzaczka.keyTab.add(key);
                        }
                        key = "" + znak;
                        sprawdzaczka.keyTab.add(key);
                        key = null;
                        possibleComment = false;
                        break;
                    case 60:   // "<"
                        if (key != null) {
                            sprawdzaczka.keyTab.add(key);
                        }
                        key = "" + znak;
                        sprawdzaczka.keyTab.add(key);
                        key = null;
                        possibleComment = false;
                        break;
                    case 62:   // ">"
                        if (key != null) {
                            sprawdzaczka.keyTab.add(key);
                        }
                        key = "" + znak;
                        sprawdzaczka.keyTab.add(key);
                        key = null;
                        possibleComment = false;
                        break;
                    default:
                        if (key == null) {
                            key = "" + znak;
                        } else {
                            key += znak;
                        }
                        possibleComment = false;
                        break;
                }
            }
            if (key != null) {
                sprawdzaczka.keyTab.add(key);
            }
        }finally{
			if(in!=null){
				in.close();
			}
		}
        //sprawdzaczka.wypisz();
        if (sprawdzaczka.packages()) {
            System.out.println("uzyto pakietow");
        }else{
			System.out.println("OK - brak pakietow");
		}
        if (sprawdzaczka.publicClass()) {
            System.out.println("uzyto klasy publicznej");
        }else{
			System.out.println("OK - brak klas publicznych");
		}
        if (sprawdzaczka.publicInterface()) {
            System.out.println("uzyto interfejsu publicznego");
        }else{
			System.out.println("OK - brak interfejsow publicznych");
		}
        if (sprawdzCzyStart) {
            if (sprawdzaczka.startClass()) {
                System.out.println("nie ma klasy Start lub metody main - ale czy mial sie pojawic ?");
            }else{
			System.out.println("OK - istnieje klasa klasa Start wraz z metoda main");
			}
        }
        if (sprawdzaczka.importy() != null) {
            System.out.println(sprawdzaczka.importy());
        }else{
			System.out.println("OK - wszystko co potrzebne zaimportowano");
		}
        if (sprawdzaczka.klamry() != null) {
            System.out.println(sprawdzaczka.klamry());
        }else{
			System.out.println("OK - klamry i nawiasy sa pozamykane");
		}
    }
}
