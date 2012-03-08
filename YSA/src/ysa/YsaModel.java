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
	
	public static void main(String[] args) {
		
		// Giriþ ve ara katman sayýlarýnýn alýnmasý...
		int girisSayisi, araKatmanSayisi;
		Scanner okunanDeger = new Scanner(System.in);
		System.out.print("Giriþ Sayýsý: ");
		girisSayisi = okunanDeger.nextInt();
		System.out.print("Ara Katman Sayýsý: ");
		araKatmanSayisi = okunanDeger.nextInt();
		System.out.println();
		
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
		
		System.out.println();
		// ara katman netlerinin hesaplanmasý
		netHesapla(girisSayisi, araKatmanSayisi, "arakatman");
		
		System.out.println();
		// ara katman f(net) deðerlerinin hesaplanmasý...
		fNetHesapla("arakatman");
		
		// Tek çýkýþ için net deðerin hesaplanmasý...
		netHesapla(araKatmanSayisi, 1, "cikis"); // 1 => çýkýþ sayýsý
		
		// Tek çýkýþ için f(net) deðerinin hesaplanmasý...
		fNetHesapla("cikis");
		
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
			System.out.println("\nÇýkýþ Net Deðeri = " + cikisNetDegeri);
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
			System.out.println("\nÇýkýþ f(Net) = " + cikisfNetDegeri);
		}
	}
	
	public static double fnet(Double net){
		return 1 / (1 + (Math.pow(Math.E, (-1*net))));
	}
	
}
