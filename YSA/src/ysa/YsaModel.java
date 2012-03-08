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
	public static double girisAraKatmanAgirliklari[][];
	public static double araKatmanCikisAgirliklari[][];
	public static ArrayList<Double> girisAraKatmanBiasleri = new ArrayList<Double>();
	public static ArrayList<Double> araKatmanCikisBiasleri = new ArrayList<Double>();
	public static double cikisNetDegeri = 0.0;
	public static double cikisfNetDegeri = 0.0;
	
	public static void main(String[] args) {
		
		// Giri� ve ara katman say�lar�n�n al�nmas�...
		int girisSayisi, araKatmanSayisi;
		Scanner okunanDeger = new Scanner(System.in);
		System.out.print("Giri� Say�s�: ");
		girisSayisi = okunanDeger.nextInt();
		System.out.print("Ara Katman Say�s�: ");
		araKatmanSayisi = okunanDeger.nextInt();
		System.out.println();
		
		girisAraKatmanAgirliklari = new double[girisSayisi][araKatmanSayisi];
		araKatmanCikisAgirliklari = new double[araKatmanSayisi][1];  // ��k�� n olunca 1 g�ncellenecek.		

		// Giri� De�erlerinin Okunmas�...
		for (int i = 0; i < girisSayisi; i++) {
			System.out.print(i+1 + ". Giri� De�eri = ");
			girisDegerleri.add(okunanDeger.nextDouble());
		}
		
		System.out.println("\n---Giri�-Ara Katman A��rl�klar�---");
		girisAraKatmanAgirliklari = agirlikAta(girisSayisi, araKatmanSayisi);
		System.out.println("\n---Ara Katman-��k�� A��rl�klar�---");
		araKatmanCikisAgirliklari = agirlikAta(araKatmanSayisi, 1);  // ��k�� n olunca 1 g�ncellenecek.
		
		System.out.println("\n---Giri�-Ara Katman Biasleri---");
		girisAraKatmanBiasleri = biasAta(araKatmanSayisi);
		System.out.println("\n---Ara Katman-��k�� Biasleri---");
		araKatmanCikisBiasleri = biasAta(1); // 1 asl�nda ��k�� say�s�...
		
		System.out.println();
		// ara katman netlerinin hesaplanmas�
		netHesapla(girisSayisi, araKatmanSayisi, "arakatman");
		
		System.out.println();
		// ara katman f(net) de�erlerinin hesaplanmas�...
		fNetHesapla("arakatman");
		
		// Tek ��k�� i�in net de�erin hesaplanmas�...
		netHesapla(araKatmanSayisi, 1, "cikis"); // 1 => ��k�� say�s�
		
		// Tek ��k�� i�in f(net) de�erinin hesaplanmas�...
		fNetHesapla("cikis");
		
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
			for (int i = 0; i < solKatmanSayisi; i++) {
				netDeger += araKatmanfNetleri.get(i) * araKatmanCikisAgirliklari[i][0];
			}
			netDeger += araKatmanCikisBiasleri.get(0); // 0, ��k�� say�s� art�nca g�ncellencek.
			cikisNetDegeri = netDeger;
			System.out.println("\n��k�� Net De�eri = " + cikisNetDegeri);
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
			cikisfNetDegeri = fnet(cikisNetDegeri);
			System.out.println("\n��k�� f(Net) = " + cikisfNetDegeri);
		}
	}
	
	public static double fnet(Double net){
		return 1 / (1 + (Math.pow(Math.E, (-1*net))));
	}
	
}
