/*
 * TODO: Geri yay�l�ml� k�s�m kodlanacak.
 */

package ysa;

import java.util.ArrayList;
import java.util.Scanner;

/**
 * Giris ve ara katman sayisinin kullanicidan okundugu bir 
 * yapay sinir agi modeli icin cikis degerini hesaplar.
 * 
 * @author Mustafa Sahin
 * 
 */
public class YsaModel {

	public static ArrayList<Double> girisDegerleri = new ArrayList<Double>();
	public static ArrayList<Double> araKatmanNetleri = new ArrayList<Double>();
	public static ArrayList<Double> araKatmanfNetleri = new ArrayList<Double>();
	public static ArrayList<Double> cikisNetleri = new ArrayList<Double>();
	public static ArrayList<Double> cikisfNetleri = new ArrayList<Double>();
	public static double girisAraKatmanAgirliklari[][];
	public static double araKatmanCikisAgirliklari[][];
	public static ArrayList<Double> girisAraKatmanBiasleri = new ArrayList<Double>();
	public static ArrayList<Double> araKatmanCikisBiasleri = new ArrayList<Double>();
//	public static double cikisNetDegeri = 0.0;
//	public static double cikisfNetDegeri = 0.0;
	public static double E = 0.0;
//	public static double cikisAraKatmanSigmaDegeri = 0.0;
	public static ArrayList<Double> cikisAraKatmanDeltaAgirlikDegerleri = new ArrayList<Double>();
	public static double alfa;
	public static double lambda;
	public static ArrayList<Double> cikisAraKatmanDeltaBiasDegerleri = new ArrayList<Double>();
	public static ArrayList<Double> cikisAraKatmanSigmaDegerleri = new ArrayList<Double>();
	public static ArrayList<Double> girisAraKatmanSigmaDegerleri = new ArrayList<Double>();
	public static ArrayList<Double> girisAraKatmanDeltaAgirlikDegerleri = new ArrayList<Double>();
	public static ArrayList<Double> girisAraKatmanDeltaBiasDegerleri = new ArrayList<Double>();
	public static double toplamHata = 0.0;
	public static double tolerans = 0.001;
	
	public static void main(String[] args) {
		
		// Giri� ve ara katman say�lar�n�n al�nmas�...
		int girisSayisi, araKatmanSayisi, cikisSayisi;
		Scanner okunanDeger = new Scanner(System.in);
		System.out.print("Giri� Say�s�: ");
		girisSayisi = okunanDeger.nextInt();
		System.out.print("Ara Katman Say�s�: ");
		araKatmanSayisi = okunanDeger.nextInt();
		System.out.print("��k�� Say�s�: ");
		cikisSayisi = okunanDeger.nextInt();
		
		
		System.out.println("\n----- �leri Yay�l�m -----\n");
		
		girisAraKatmanAgirliklari = new double[girisSayisi][araKatmanSayisi];
		araKatmanCikisAgirliklari = new double[araKatmanSayisi][cikisSayisi];
		
		// Giri� De�erlerinin Okunmas�...
		for (int i = 0; i < girisSayisi; i++) {
			System.out.print(i+1 + ". Giri� De�eri = ");
			girisDegerleri.add(okunanDeger.nextDouble());
		}
		
		System.out.println("\n---Giri�-Ara Katman A��rl�klar�---");
		girisAraKatmanAgirliklari = agirlikAta(girisSayisi, araKatmanSayisi);
		System.out.println("\n---Ara Katman-��k�� A��rl�klar�---");
		araKatmanCikisAgirliklari = agirlikAta(araKatmanSayisi, cikisSayisi);
		
		System.out.println("\n---Giri�-Ara Katman Biasleri---");
		girisAraKatmanBiasleri = biasAta(araKatmanSayisi);
		System.out.println("\n---Ara Katman-��k�� Biasleri---");
		araKatmanCikisBiasleri = biasAta(cikisSayisi);
		
		/*
		 * Geri yay�l�ml� hesaplamada �arpan olarak kullan�lacak
		 * olan alfa ve lambda de�erleri okunuyor.
		 */
		
		System.out.print("Alfa = ");
		alfa = okunanDeger.nextDouble();
		System.out.print("Lambda = ");
		lambda = okunanDeger.nextDouble();
		
		// Beklenen de�erin girilmesi...
		System.out.print("Beklenen De�er = ");
		int beklenenDeger = okunanDeger.nextInt();
		
		/*
		 * �leri Yay�l�m
		 */
		
		// ara katman netlerinin hesaplanmas�
		System.out.println("\n---Ara katman NET de�erleri---");
		netHesapla(girisSayisi, araKatmanSayisi, "arakatman");
		
		// ara katman f(net) de�erlerinin hesaplanmas�...
		System.out.println("\n---Ara katman f(NET) de�erleri---");
		fNetHesapla("arakatman");
		
		// ��k��lar i�in net de�erin hesaplanmas�...
		System.out.println("\n---��k�� NET de�erleri---");
		netHesapla(araKatmanSayisi, cikisSayisi, "cikis"); // 1 => ��k�� say�s�
		
		// Tek ��k�� i�in f(net) de�erinin hesaplanmas�...
		System.out.println("\n---��k�� f(NET) de�erleri---");
		fNetHesapla("cikis");
		
		/*
		 * �leri yay�l�m sonu
		 * Geri yay�l�m ba�lang�c�
		 */
		
		System.out.println("\n----- Geri Yay�l�m -----\n");
		
		// Toplam hatan�n hesaplanmas�...
		toplamHata = toplamHataHesapla(beklenenDeger);
		System.out.println("Toplam hata = " + toplamHata + "\n");
		
		/*
		 * ��k��-Ara katman aras� geri yay�l�m i�lemleri...
		 */
		
		// ��k��-Ara katman aras� sigma de�erleri hesaplan�yor...
		sigmaHesapla("cikis-araKatman");		
		System.out.println();
		
		// ��k��-Ara Katman aras� a��rl�k de�i�imleri hesaplan�yor...
		deltaAgirlikHesapla("cikis-araKatman");
		
		// ��k��-Ara Katman aras� bias de�i�imleri hesaplan�yor...
		deltaBiasHesapla("cikis-araKatman");
		
		System.out.println("-------------------------------------------\n");
		
		// Giri�-Ara Katman aras� sigma de�erleri hesaplan�yor...
		sigmaHesapla("giris-araKatman");
		
		// Giri�-Ara Katman aras� a��rl�k de�i�imleri hesaplan�yor...
		deltaAgirlikHesapla("giris-araKatman");
		
		// Giri�-Ara katman aras� bias de�i�imleri hesaplan�yor...
		deltaBiasHesapla("giris-araKatman");
			
	}
	
	public static double toplamHataHesapla(int beklenenDeger){
		ArrayList<Double> hatalar = new ArrayList<Double>();
		for (int i = 0; i < cikisfNetleri.size(); i++) {
			hatalar.add(beklenenDeger - cikisfNetleri.get(i));
		}
		double araDeger = 0.0;
		for (int i = 0; i < hatalar.size(); i++) {
			araDeger += Math.pow(hatalar.get(i), 2);
		}
		
		return Math.sqrt(araDeger); 
	}
	
	public static void sigmaHesapla(String gorev){
		// Buras� sorunlu...
		if (gorev.equals("giris-araKatman")){
			double sigmaDegeri = 0.0;
			for (int i = 0; i < araKatmanfNetleri.size(); i++) {
				sigmaDegeri = araKatmanfNetleri.get(i) * (1 - araKatmanfNetleri.get(i)) * cikisAraKatmanSigmaDegerleri.get(i) * araKatmanCikisAgirliklari[i][0];
				System.out.println("Giri�-Ara Katman aras� " + (i+1) + ". Sigma De�eri = " + sigmaDegeri);
				girisAraKatmanSigmaDegerleri.add(sigmaDegeri);
			}
			System.out.println();
		}
		else if (gorev.equals("cikis-araKatman")){
			double sigmaDegeri = 0.0;
			for (int i = 0; i < cikisfNetleri.size(); i++) {
				sigmaDegeri = cikisfNetleri.get(i) * (1 - cikisfNetleri.get(i)) * toplamHata;
				System.out.println("��k��-Ara Katman aras� " + i+1 + ". Sigma De�eri = " + sigmaDegeri);
				cikisAraKatmanSigmaDegerleri.add(sigmaDegeri);
			}
		}
	}
	
	public static void deltaBiasHesapla(String gorev){
		if (gorev.equals("cikis-araKatman")){
			double deltaBiasDegeri = 0.0;
			for (int i = 0; i < araKatmanCikisBiasleri.size(); i++) {
				deltaBiasDegeri = lambda * cikisAraKatmanSigmaDegerleri.get(i);
				System.out.println("��k��-Ara Katman aras� " + (i+1) + ". deltaBias = " + deltaBiasDegeri);
				cikisAraKatmanDeltaBiasDegerleri.add(deltaBiasDegeri);
			}
		}
		else if (gorev.equals("giris-araKatman")){
			double deltaBiasDegeri = 0.0;
			for (int i = 0; i < girisAraKatmanBiasleri.size(); i++) {
				deltaBiasDegeri = lambda * girisAraKatmanSigmaDegerleri.get(i);
				System.out.println("Giri�-Ara Katman aras� " + (i+1) + ". deltaBias = " + deltaBiasDegeri);
				girisAraKatmanDeltaBiasDegerleri .add(deltaBiasDegeri);
			}
		}
		System.out.println();
	}
	
	public static void deltaAgirlikHesapla(String gorev){
		if (gorev.equals("cikis-araKatman")){
			double deltaAgirlikDegeri = 0.0;
			int indis = 0;
			for (int i = 0; i < araKatmanCikisAgirliklari.length; i++) {
				for (int j = 0; j < araKatmanCikisAgirliklari.length; j++) {
					deltaAgirlikDegeri = lambda * cikisAraKatmanSigmaDegerleri.get(j) * araKatmanfNetleri.get(j);
					System.out.println("��k��-Ara Katman aras� " + (indis+1) + ". deltaA��rl�k = " + deltaAgirlikDegeri);
					cikisAraKatmanDeltaAgirlikDegerleri.add(deltaAgirlikDegeri);
					indis++;
				}
			}
		}
		else if (gorev.equals("giris-araKatman")){
			double deltaAgirlikDegeri = 0.0;
			int indis = 0;
			for (int i = 0; i < girisAraKatmanAgirliklari.length; i++) {
				for (int j = 0; j < girisAraKatmanAgirliklari.length; j++) {
					deltaAgirlikDegeri = lambda * girisAraKatmanSigmaDegerleri.get(j) * girisDegerleri.get(j);
					System.out.println("Giri�-Ara Katman aras� " + (indis+1) + ". deltaA��rl�k = " + deltaAgirlikDegeri);
					girisAraKatmanDeltaAgirlikDegerleri.add(deltaAgirlikDegeri);
					indis++;
				}
			}
		}
		System.out.println();
	}

	public static double[][] agirlikAta(int satirSayisi, int sutunSayisi) {
		double matris[][] = new double[satirSayisi][sutunSayisi];
		double rastgele = 0.0; // Bilgilendirme i�in...
		for (int i = 0; i < satirSayisi; i++) {
			for (int j = 0; j < sutunSayisi; j++) {
				rastgele = Math.random();
				matris[i][j] = rastgele;
				System.out.println("A��rl�k[" + i + "][" + j + "] = " + rastgele);
			}
		}
		return matris;
	}
	
	public static ArrayList<Double> biasAta(int uzunluk){
		ArrayList<Double> list = new ArrayList<Double>();
		double rastgele = 0.0;
		for (int i = 0; i < uzunluk; i++) {
			rastgele = Math.random();
			list.add(rastgele);
			System.out.println(i+1 + ". Bias De�eri = " + rastgele);
		}
		return list;
	}
	
	public static void netHesapla(int solKatmanSayisi, int sagKatmanSayisi, String gorev){
		if (gorev.equals("arakatman")){
			double netDeger = 0.0; // hesaplanacak olan net de�erinin tutulmas� ve net dizisine at�lmas� i�in... 
			for (int i = 0; i < sagKatmanSayisi; i++) {
				for (int j = 0; j < solKatmanSayisi; j++) {
					netDeger += girisDegerleri.get(j) * girisAraKatmanAgirliklari[j][i];
				}
				netDeger += girisAraKatmanBiasleri.get(i); // Bias eklendi.
				araKatmanNetleri.add(netDeger);
				System.out.println(i+1 + ". Ara Katman Net De�eri = " + netDeger);
				netDeger = 0.0;
			}
		}
		else if (gorev.equals("cikis")){
			double netDeger = 0.0;
			for (int i = 0; i < sagKatmanSayisi; i++) {
				for (int j = 0; j < solKatmanSayisi; j++) {
					netDeger += araKatmanfNetleri.get(j) * araKatmanCikisAgirliklari[j][i];
				}
				netDeger += araKatmanCikisBiasleri.get(i);
				cikisNetleri.add(netDeger);
				System.out.println(i+1 + ". ��k�� Net De�eri = " + netDeger);
				netDeger = 0.0;
			}
		}
	}
	
	public static void fNetHesapla(String gorev){
		if (gorev.equals("arakatman")){
			double fnetDeger = 0.0;
			for (int i = 0; i < araKatmanNetleri.size(); i++) {
				fnetDeger = fnet(araKatmanNetleri.get(i)); 
				araKatmanfNetleri.add(fnetDeger);
				System.out.println(i+1 + ". Ara Katman  ��in f(Net) = " + fnetDeger);
			}
		}
		else if (gorev.equals("cikis")){
			double fnetDeger = 0.0;
			for (int i = 0; i < cikisNetleri.size(); i++) {
				fnetDeger = fnet(cikisNetleri.get(i));
				cikisfNetleri.add(fnetDeger);
				System.out.println(i+1 + ". ��k�� ��in f(NET) = " + fnetDeger);
			}
		}
	}
	
	public static double fnet(Double net){
		return 1 / (1 + (Math.pow(Math.E, (-1*net))));
	}
	
}
