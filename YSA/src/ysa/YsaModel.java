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
	public static double girisAraKatmanAgirliklari[][];
	public static double araKatmanCikisAgirliklari[][];
	public static ArrayList<Double> girisAraKatmanBiasleri = new ArrayList<Double>();
	public static ArrayList<Double> araKatmanCikisBiasleri = new ArrayList<Double>();
	public static double cikisNetDegeri = 0.0;
	public static double cikisfNetDegeri = 0.0;
	public static double E = 0.0;
	public static double cikisAraKatmanSigmaDegeri = 0.0;
	public static ArrayList<Double> cikisAraKatmanDeltaAgirlikDegerleri = new ArrayList<Double>();
	public static double alfa;
	public static double lambda;
	public static ArrayList<Double> cikisAraKatmanDeltaBiasDegerleri = new ArrayList<Double>();
	public static ArrayList<Double> girisAraKatmanSigmaDegerleri = new ArrayList<Double>();
	public static ArrayList<Double> girisAraKatmanDeltaAgirlikDegerleri = new ArrayList<Double>();
	public static ArrayList<Double> girisAraKatmanDeltaBiasDegerleri = new ArrayList<Double>();
	
	public static void main(String[] args) {
		
		// Giriþ ve ara katman sayýlarýnýn alýnmasý...
		int girisSayisi, araKatmanSayisi;
		Scanner okunanDeger = new Scanner(System.in);
		System.out.print("Giriþ Sayýsý: ");
		girisSayisi = okunanDeger.nextInt();
		System.out.print("Ara Katman Sayýsý: ");
		araKatmanSayisi = okunanDeger.nextInt();
		
		/*
		 * Ýleri Yayýlým
		 */
		
		System.out.println("\n----- Ýleri Yayýlým -----\n");
		
		girisAraKatmanAgirliklari = new double[girisSayisi][araKatmanSayisi];
		araKatmanCikisAgirliklari = new double[araKatmanSayisi][1];  // çýkýþ n olunca 1 güncellenecek.		

		// Giriþ Deðerlerinin Okunmasý...
		for (int i = 0; i < girisSayisi; i++) {
			System.out.print(i+1 + ". Giriþ Deðeri = ");
			girisDegerleri.add(okunanDeger.nextDouble());
		}
		
		System.out.println("\n---Giriþ-Ara Katman Aðýrlýklarý---");
		girisAraKatmanAgirliklari = agirlikAta(girisSayisi, araKatmanSayisi);
		System.out.println("\n---Ara Katman-Çýkýþ Aðýrlýklarý---");
		araKatmanCikisAgirliklari = agirlikAta(araKatmanSayisi, 1);  // çýkýþ n olunca 1 güncellenecek.
		
		System.out.println("\n---Giriþ-Ara Katman Biasleri---");
		girisAraKatmanBiasleri = biasAta(araKatmanSayisi);
		System.out.println("\n---Ara Katman-Çýkýþ Biasleri---");
		araKatmanCikisBiasleri = biasAta(1); // 1 aslýnda çýkýþ sayýsý...
		
		// ara katman netlerinin hesaplanmasý
		System.out.println("\n---Ara katman NET deðerleri---");
		netHesapla(girisSayisi, araKatmanSayisi, "arakatman");
		
		// ara katman f(net) deðerlerinin hesaplanmasý...
		System.out.println("\n---Ara katman f(NET) deðerleri---");
		fNetHesapla("arakatman");
		
		// Tek çýkýþ için net deðerin hesaplanmasý...
		netHesapla(araKatmanSayisi, 1, "cikis"); // 1 => çýkýþ sayýsý
		System.out.println("\nÇýkýþ Net Deðeri = " + cikisNetDegeri);
		
		// Tek çýkýþ için f(net) deðerinin hesaplanmasý...
		fNetHesapla("cikis");
		System.out.println("\nÇýkýþ f(Net) = " + cikisfNetDegeri);
		
		/*
		 * Ýleri yayýlým sonu
		 * Geri yayýlým baþlangýcý
		 */
		
		System.out.println("\n----- Geri Yayýlým -----\n");
		
		// Hata miktarý E'nin hesaplanmasý
		System.out.print("Beklenen Deðer = ");
		int beklenenDeger = okunanDeger.nextInt();
		E = beklenenDeger - cikisfNetDegeri;
		System.out.println("Hata Miktarý E = " + E + "\n");
		
		/*
		 * Çýkýþ-Ara katman arasý geri yayýlým iþlemleri...
		 */
		
		// Çýkýþýmýz tek olduðu için tek deðiþkende tutulan sigma deðeri
		// sigma1 = C1*(1-C1)*E
		cikisAraKatmanSigmaDegeri = cikisfNetDegeri * (1 - cikisfNetDegeri) * E;
		System.out.println("Çýkýþ-Ara Katman Arasý Sigma Deðeri = " + cikisAraKatmanSigmaDegeri + "\n");

		/*
		 * Geri yayýlýmlý hesaplamada çarpan olarak kullanýlacak
		 * olan alfa ve lambda deðerleri okunuyor.
		 */
//		Scanner okunanDeger2 = new Scanner(System.in);
		System.out.print("Alfa = ");
		alfa = okunanDeger.nextDouble();
		System.out.print("Lambda = ");
		lambda = okunanDeger.nextDouble();
		
		System.out.println();
		
		// Çýkýþ-Ara Katman arasý aðýrlýk deðiþimleri hesaplanýyor...
		deltaAgirlikHesapla("cikis-araKatman");
		
		// Çýkýþ-Ara Katman arasý bias deðiþimleri hesaplanýyor...
		deltaBiasHesapla("cikis-araKatman");
		
		System.out.println("-------------------------------------------\n");
		
		// Giriþ-Ara Katman arasý sigma deðerleri hesaplanýyor...
		sigmaHesapla();
		
		// Giriþ-Ara Katman arasý aðýrlýk deðiþimleri hesaplanýyor...
		deltaAgirlikHesapla("giris-araKatman");
		
		// Giriþ-Ara katman arasý bias deðiþimleri hesaplanýyor...
		deltaBiasHesapla("giris-araKatman");
			
	}
	
	public static void sigmaHesapla(){
		double sigmaDegeri = 0.0;
		for (int i = 0; i < araKatmanfNetleri.size(); i++) {
			sigmaDegeri = araKatmanfNetleri.get(i) * (1 - araKatmanfNetleri.get(i) * cikisAraKatmanSigmaDegeri * araKatmanCikisAgirliklari[i][0]);
			System.out.println("Giriþ-Ara Katman arasý " + (i+1) + ". Sigma Deðeri = " + sigmaDegeri);
			girisAraKatmanSigmaDegerleri.add(sigmaDegeri);
		}
		System.out.println();
	}
	
	public static void deltaBiasHesapla(String gorev){
		if (gorev.equals("cikis-araKatman")){
			double deltaBiasDegeri = lambda * cikisAraKatmanSigmaDegeri;
			for (int i = 0; i < araKatmanCikisBiasleri.size(); i++) {
				deltaBiasDegeri = lambda * cikisAraKatmanSigmaDegeri;
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
			for (int i = 0; i < araKatmanCikisAgirliklari.length; i++) {
				deltaAgirlikDegeri = lambda * cikisAraKatmanSigmaDegeri * araKatmanfNetleri.get(i);
				System.out.println("Çýkýþ-Ara Katman arasý " + (i+1) + ". deltaAðýrlýk = " + deltaAgirlikDegeri);
				cikisAraKatmanDeltaAgirlikDegerleri.add(deltaAgirlikDegeri);
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
			for (int i = 0; i < solKatmanSayisi; i++) {
				netDeger += araKatmanfNetleri.get(i) * araKatmanCikisAgirliklari[i][0];
			}
			netDeger += araKatmanCikisBiasleri.get(0); // 0, çýkýþ sayýsý artýnca güncellencek.
			cikisNetDegeri = netDeger;
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
			cikisfNetDegeri = fnet(cikisNetDegeri);
		}
	}
	
	public static double fnet(Double net){
		return 1 / (1 + (Math.pow(Math.E, (-1*net))));
	}
	
}
