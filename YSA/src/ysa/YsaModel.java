/*
 * TODO: Geri yayýlýmlý kýsým kodlanacak.
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
		
		// Giriþ ve ara katman sayýlarýnýn alýnmasý...
		int girisSayisi, araKatmanSayisi, cikisSayisi;
		Scanner okunanDeger = new Scanner(System.in);
		System.out.print("Giriþ Sayýsý: ");
		girisSayisi = okunanDeger.nextInt();
		System.out.print("Ara Katman Sayýsý: ");
		araKatmanSayisi = okunanDeger.nextInt();
		System.out.print("Çýkýþ Sayýsý: ");
		cikisSayisi = okunanDeger.nextInt();
		
		
		System.out.println("\n----- Ýleri Yayýlým -----\n");
		
		girisAraKatmanAgirliklari = new double[girisSayisi][araKatmanSayisi];
		araKatmanCikisAgirliklari = new double[araKatmanSayisi][cikisSayisi];
		
		// Giriþ Deðerlerinin Okunmasý...
		for (int i = 0; i < girisSayisi; i++) {
			System.out.print(i+1 + ". Giriþ Deðeri = ");
			girisDegerleri.add(okunanDeger.nextDouble());
		}
		
		System.out.println("\n---Giriþ-Ara Katman Aðýrlýklarý---");
		girisAraKatmanAgirliklari = agirlikAta(girisSayisi, araKatmanSayisi);
		System.out.println("\n---Ara Katman-Çýkýþ Aðýrlýklarý---");
		araKatmanCikisAgirliklari = agirlikAta(araKatmanSayisi, cikisSayisi);
		
		System.out.println("\n---Giriþ-Ara Katman Biasleri---");
		girisAraKatmanBiasleri = biasAta(araKatmanSayisi);
		System.out.println("\n---Ara Katman-Çýkýþ Biasleri---");
		araKatmanCikisBiasleri = biasAta(cikisSayisi);
		
		/*
		 * Geri yayýlýmlý hesaplamada çarpan olarak kullanýlacak
		 * olan alfa ve lambda deðerleri okunuyor.
		 */
		
		System.out.print("Alfa = ");
		alfa = okunanDeger.nextDouble();
		System.out.print("Lambda = ");
		lambda = okunanDeger.nextDouble();
		
		// Beklenen deðerin girilmesi...
		System.out.print("Beklenen Deðer = ");
		int beklenenDeger = okunanDeger.nextInt();
		
		/*
		 * Ýleri Yayýlým
		 */
		
		// ara katman netlerinin hesaplanmasý
		System.out.println("\n---Ara katman NET deðerleri---");
		netHesapla(girisSayisi, araKatmanSayisi, "arakatman");
		
		// ara katman f(net) deðerlerinin hesaplanmasý...
		System.out.println("\n---Ara katman f(NET) deðerleri---");
		fNetHesapla("arakatman");
		
		// Çýkýþlar için net deðerin hesaplanmasý...
		System.out.println("\n---Çýkýþ NET deðerleri---");
		netHesapla(araKatmanSayisi, cikisSayisi, "cikis"); // 1 => çýkýþ sayýsý
		
		// Tek çýkýþ için f(net) deðerinin hesaplanmasý...
		System.out.println("\n---Çýkýþ f(NET) deðerleri---");
		fNetHesapla("cikis");
		
		/*
		 * Ýleri yayýlým sonu
		 * Geri yayýlým baþlangýcý
		 */
		
		System.out.println("\n----- Geri Yayýlým -----\n");
		
		// Toplam hatanýn hesaplanmasý...
		toplamHata = toplamHataHesapla(beklenenDeger);
		System.out.println("Toplam hata = " + toplamHata + "\n");
		
		/*
		 * Çýkýþ-Ara katman arasý geri yayýlým iþlemleri...
		 */
		
		// Çýkýþ-Ara katman arasý sigma deðerleri hesaplanýyor...
		sigmaHesapla("cikis-araKatman");		
		System.out.println();
		
		// Çýkýþ-Ara Katman arasý aðýrlýk deðiþimleri hesaplanýyor...
		deltaAgirlikHesapla("cikis-araKatman");
		
		// Çýkýþ-Ara Katman arasý bias deðiþimleri hesaplanýyor...
		deltaBiasHesapla("cikis-araKatman");
		
		System.out.println("-------------------------------------------\n");
		
		// Giriþ-Ara Katman arasý sigma deðerleri hesaplanýyor...
		sigmaHesapla("giris-araKatman");
		
		// Giriþ-Ara Katman arasý aðýrlýk deðiþimleri hesaplanýyor...
		deltaAgirlikHesapla("giris-araKatman");
		
		// Giriþ-Ara katman arasý bias deðiþimleri hesaplanýyor...
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
		// Burasý sorunlu...
		if (gorev.equals("giris-araKatman")){
			double sigmaDegeri = 0.0;
			for (int i = 0; i < araKatmanfNetleri.size(); i++) {
				sigmaDegeri = araKatmanfNetleri.get(i) * (1 - araKatmanfNetleri.get(i)) * cikisAraKatmanSigmaDegerleri.get(i) * araKatmanCikisAgirliklari[i][0];
				System.out.println("Giriþ-Ara Katman arasý " + (i+1) + ". Sigma Deðeri = " + sigmaDegeri);
				girisAraKatmanSigmaDegerleri.add(sigmaDegeri);
			}
			System.out.println();
		}
		else if (gorev.equals("cikis-araKatman")){
			double sigmaDegeri = 0.0;
			for (int i = 0; i < cikisfNetleri.size(); i++) {
				sigmaDegeri = cikisfNetleri.get(i) * (1 - cikisfNetleri.get(i)) * toplamHata;
				System.out.println("Çýkýþ-Ara Katman arasý " + i+1 + ". Sigma Deðeri = " + sigmaDegeri);
				cikisAraKatmanSigmaDegerleri.add(sigmaDegeri);
			}
		}
	}
	
	public static void deltaBiasHesapla(String gorev){
		if (gorev.equals("cikis-araKatman")){
			double deltaBiasDegeri = 0.0;
			for (int i = 0; i < araKatmanCikisBiasleri.size(); i++) {
				deltaBiasDegeri = lambda * cikisAraKatmanSigmaDegerleri.get(i);
				System.out.println("Çýkýþ-Ara Katman arasý " + (i+1) + ". deltaBias = " + deltaBiasDegeri);
				cikisAraKatmanDeltaBiasDegerleri.add(deltaBiasDegeri);
			}
		}
		else if (gorev.equals("giris-araKatman")){
			double deltaBiasDegeri = 0.0;
			for (int i = 0; i < girisAraKatmanBiasleri.size(); i++) {
				deltaBiasDegeri = lambda * girisAraKatmanSigmaDegerleri.get(i);
				System.out.println("Giriþ-Ara Katman arasý " + (i+1) + ". deltaBias = " + deltaBiasDegeri);
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
					System.out.println("Çýkýþ-Ara Katman arasý " + (indis+1) + ". deltaAðýrlýk = " + deltaAgirlikDegeri);
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
					System.out.println("Giriþ-Ara Katman arasý " + (indis+1) + ". deltaAðýrlýk = " + deltaAgirlikDegeri);
					girisAraKatmanDeltaAgirlikDegerleri.add(deltaAgirlikDegeri);
					indis++;
				}
			}
		}
		System.out.println();
	}

	public static double[][] agirlikAta(int satirSayisi, int sutunSayisi) {
		double matris[][] = new double[satirSayisi][sutunSayisi];
		double rastgele = 0.0; // Bilgilendirme için...
		for (int i = 0; i < satirSayisi; i++) {
			for (int j = 0; j < sutunSayisi; j++) {
				rastgele = Math.random();
				matris[i][j] = rastgele;
				System.out.println("Aðýrlýk[" + i + "][" + j + "] = " + rastgele);
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
			System.out.println(i+1 + ". Bias Deðeri = " + rastgele);
		}
		return list;
	}
	
	public static void netHesapla(int solKatmanSayisi, int sagKatmanSayisi, String gorev){
		if (gorev.equals("arakatman")){
			double netDeger = 0.0; // hesaplanacak olan net deðerinin tutulmasý ve net dizisine atýlmasý için... 
			for (int i = 0; i < sagKatmanSayisi; i++) {
				for (int j = 0; j < solKatmanSayisi; j++) {
					netDeger += girisDegerleri.get(j) * girisAraKatmanAgirliklari[j][i];
				}
				netDeger += girisAraKatmanBiasleri.get(i); // Bias eklendi.
				araKatmanNetleri.add(netDeger);
				System.out.println(i+1 + ". Ara Katman Net Deðeri = " + netDeger);
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
				System.out.println(i+1 + ". Çýkýþ Net Deðeri = " + netDeger);
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
				System.out.println(i+1 + ". Ara Katman  Ýçin f(Net) = " + fnetDeger);
			}
		}
		else if (gorev.equals("cikis")){
			double fnetDeger = 0.0;
			for (int i = 0; i < cikisNetleri.size(); i++) {
				fnetDeger = fnet(cikisNetleri.get(i));
				cikisfNetleri.add(fnetDeger);
				System.out.println(i+1 + ". Çýkýþ Ýçin f(NET) = " + fnetDeger);
			}
		}
	}
	
	public static double fnet(Double net){
		return 1 / (1 + (Math.pow(Math.E, (-1*net))));
	}
	
}
