//package utr_lab3;

import java.io.File;
import java.io.FileNotFoundException;
import java.security.KeyStore.Entry;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Objects;
import java.util.Scanner;

public class SimPa {

	public static class StanjeZnakStog {
		public String trenutnoStanje;
		public String ulazniZnak;
		public String znakStoga;

		public StanjeZnakStog(String trenutnoStanje, String ulazniZnak, String znakStoga) {
			this.trenutnoStanje = trenutnoStanje;
			this.ulazniZnak = ulazniZnak;
			this.znakStoga = znakStoga;
		}

		@Override
		public String toString() {
			return trenutnoStanje + "," + ulazniZnak + "," + znakStoga;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj) {
				return true;
			}

			if (obj == null || getClass() != obj.getClass()) {
				return false;
			}

			StanjeZnakStog other = (StanjeZnakStog) obj;

			// Compare relevant fields for equality
			return other.trenutnoStanje.equals(this.trenutnoStanje) && other.ulazniZnak.equals(this.ulazniZnak)
					&& other.znakStoga.equals(this.znakStoga);
		}

		@Override
		public int hashCode() {
			return Objects.hash(trenutnoStanje, ulazniZnak, znakStoga);
		}
	}

	public static class NovoStanjeNizZnakovaStoga {
		String novoStanje;
		String nizZnakovaStoga;

		public NovoStanjeNizZnakovaStoga(String novoStanje, String nizZnakovaStoga) {
			this.novoStanje = novoStanje;
			this.nizZnakovaStoga = nizZnakovaStoga;
		}

		@Override
		public String toString() {
			return novoStanje + "," + nizZnakovaStoga;
		}
	}

	public static void main(String[] args) throws FileNotFoundException {
		
		File file = new File("src/utr_lab3/primjer.txt");
		//Scanner sc = new Scanner(file);
		Scanner sc = new Scanner(System.in);
		
		ArrayList<String> ulazniNizovi = new ArrayList<>();
		ArrayList<String> stanja = new ArrayList<>();
		ArrayList<String> ulazniZnakovi = new ArrayList<>();
		ArrayList<String> znakoviStoga = new ArrayList<>();
		ArrayList<String> prihvatljivaStanja = new ArrayList<>();
		String pocetnoStanje = new String();
		String pocetniZnakStoga = new String();
		String stog = new String();
		LinkedHashMap<StanjeZnakStog, NovoStanjeNizZnakovaStoga> prijelazi = new LinkedHashMap<SimPa.StanjeZnakStog, NovoStanjeNizZnakovaStoga>();
		
		int i = 1;
		while (sc.hasNextLine()) {
			String line = sc.nextLine();
			if (i == 1) {
				ulazniNizovi = new ArrayList<String>(Arrays.asList(line.split("\\|")));
			}
			else if (i == 2) {
				stanja = new ArrayList<String>(Arrays.asList(line.split(",")));
			}
			else if (i == 3) {
				ulazniZnakovi = new ArrayList<String>(Arrays.asList(line.split(",")));
			}
			else if (i == 4) {
				znakoviStoga = new ArrayList<String>(Arrays.asList(line.split(",")));
			}
			else if (i == 5) {
				if (prihvatljivaStanja != null)
					prihvatljivaStanja = new ArrayList<String>(Arrays.asList(line.split(",")));
				else
					prihvatljivaStanja = null;
			}
			else if (i == 6) {
				pocetnoStanje = line;
			}
			else if (i == 7) {
				pocetniZnakStoga = line;
			}
			else if (i > 7){
				String parsedString = line.replaceAll(",", " ").replaceAll("->", " ").replaceAll("\\s+", " ");
				//System.out.println(parsedString);
				String[] splitString = parsedString.split(" ");
				String trenutnoStanje = splitString[0];
				String ulazniZnak = splitString[1];
				String znakStoga = splitString[2];
				String novoStanje = splitString[3];
				String noviStog = splitString[4];
				
				StanjeZnakStog tmp1 = new StanjeZnakStog(trenutnoStanje, ulazniZnak, znakStoga);
				NovoStanjeNizZnakovaStoga tmp2 = new NovoStanjeNizZnakovaStoga(novoStanje, noviStog);
				//System.out.println("TMP = "+tmp);
				
				prijelazi.put(tmp1, tmp2);
			}
			i++;
		}
//		System.out.println(ulazniNizovi);
//		System.out.println(stanja);
//		System.out.println(ulazniZnakovi);
//		System.out.println(znakoviStoga);
//		System.out.println(prihvatljivaStanja);
//		System.out.println(pocetnoStanje);
//		System.out.println(pocetniZnakStoga);
//		System.out.println(prijelazi);
		
		
		for (String ulazniNiz : ulazniNizovi) {
			System.out.print(pocetnoStanje+"#"+pocetniZnakStoga+"|");
			String trenutnoStanje = pocetnoStanje;
			stog = pocetniZnakStoga;
			String[] znakoviUlaznogNiza = ulazniNiz.split(",");
			int cnt = 0;
			for (int ind = 0; ind < znakoviUlaznogNiza.length; ind++) {
				String znakUlaznogNiza = znakoviUlaznogNiza[ind];
				cnt++;
				String vrhStoga = stog.substring(0, 1);
				StanjeZnakStog kljuc = new StanjeZnakStog(trenutnoStanje, znakUlaznogNiza, vrhStoga);
				if (prijelazi.containsKey(kljuc)) {
					NovoStanjeNizZnakovaStoga vrijednost = prijelazi.get(kljuc);
					trenutnoStanje = vrijednost.novoStanje;
					String noviStog = vrijednost.nizZnakovaStoga;
					if (noviStog.length() == 1) {
						if (noviStog.equals("$")) {
							stog = stog.substring(1);
							if (stog.equals("")) stog = "$";
						}
					}
					else {
						stog = noviStog + stog.substring(1);
					}
					System.out.print(trenutnoStanje+"#"+stog+"|");
				}
				else {
					ind--;
					cnt--;
					StanjeZnakStog noviKljuc = new StanjeZnakStog(trenutnoStanje, "$", vrhStoga);
					if (prijelazi.containsKey(noviKljuc)) {
						NovoStanjeNizZnakovaStoga novaVrijednost = prijelazi.get(noviKljuc);
						trenutnoStanje = novaVrijednost.novoStanje;
						String noviStog = novaVrijednost.nizZnakovaStoga;
						if (noviStog.length() == 1) {
							if (noviStog.equals("$")) {
								stog = stog.substring(1);
								if (stog.equals("")) stog = "$";
							}
						}
						else {
							stog = noviStog + stog.substring(1);
						}
						System.out.print(trenutnoStanje+"#"+stog+"|");
					}
					else {
						System.out.print("fail|0");
						break;
					}
				}
				if (cnt == znakoviUlaznogNiza.length) {
					if (prihvatljivaStanja.contains(trenutnoStanje)) System.out.print(1);
					else {
						String printaj = "";
						while (true) {
							vrhStoga = stog.substring(0, 1);
							StanjeZnakStog noviKljuc = new StanjeZnakStog(trenutnoStanje, "$", vrhStoga);
							if (prijelazi.containsKey(noviKljuc)) {
								NovoStanjeNizZnakovaStoga novaVrijednost = prijelazi.get(noviKljuc);
								trenutnoStanje = novaVrijednost.novoStanje;
								String noviStog = novaVrijednost.nizZnakovaStoga;
								if (noviStog.length() == 1) {
									if (noviStog.equals("$")) {
										stog = stog.substring(1);
										if (stog.equals("")) stog = "$";
									}
								}
								else {
									stog = noviStog + stog.substring(1);
								}
								printaj += trenutnoStanje+"#"+stog+"|";
								if (prihvatljivaStanja.contains(trenutnoStanje)) {
									System.out.print(printaj+"1");
									break;
								}
							}
							else {
								System.out.print(printaj + "0");
								break;
							}
						}
					}
				}
			}
			System.out.println();
		}
	}

}
