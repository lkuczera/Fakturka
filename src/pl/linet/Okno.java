package pl.linet;
import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.HashMap;
/**
 * @author kukems
 * @date </b>2005-12<b>
 * FIXME - przyspieszyc ten program - profilowanie
 * TODO - dodawanie wiekszej ilosci pol z usluga/towarem do faktury
 * TODO - jakies ikonki
 */
public class Okno extends JFrame implements Serializable {
	static final long serialVersionUID = 10l; 
	private static  final int 
	WINDOW_LOCATION_X = 250,
	WINDOW_LOCATION_Y = 50,
	WINDOW_WIDTH = 550,
	WINDOW_HEIGHT = 500,
	WINDOW_GRID_ROWS = 19,
	WINDOW_GRID_COLS = 2;
	
	/* elementy okna */
//	przyciski
	public static JButton
	generuj = new JButton("generuj fakture"),
	zamknij = new JButton("zamknij"),
	wyczysc = new JButton("wyczyść"),
	zapisz  = new JButton("zapisz"),
	wczytaj = new JButton("wczytaj"),
	usun = new JButton("usuń");
	
//etykiety
	private static final JLabel
	lnetto = new JLabel("Kwota netto",JLabel.LEFT),
	k_nazwa = new JLabel("Nazwa nabywcy",JLabel.LEFT),
	k_adres = new JLabel("Adres",JLabel.LEFT),
	k_nip = new JLabel("Nip",JLabel.LEFT),
	k_kod = new JLabel("Kod",JLabel.LEFT),
	k_miejsc = new JLabel("Miasto",JLabel.LEFT),
	pr_nazwa = new JLabel("Nazwa towaru/usługi",JLabel.LEFT),
	j_m = new JLabel("Jednoska miary", JLabel.LEFT),
	cena_jedn = new JLabel("Cena jednostkowa",JLabel.LEFT),
	ilosc = new JLabel("Ilość",JLabel.LEFT),
	vat = new JLabel("vat",JLabel.LEFT),
	sp_platnosci = new JLabel("Sposób zapłaty",JLabel.LEFT),
	termin_platnosci = new JLabel("Termin płatności",JLabel.LEFT),
	data_wystawienia = new JLabel("Data wystawienia",JLabel.LEFT),
	slownie = new JLabel("Do zapłaty słownie",JLabel.LEFT),
	nr_f = new JLabel("Faktura Numer",JLabel.LEFT);
	
//	kontenerek do etykiet
	private static final JLabel[] label_array = { 
	nr_f,data_wystawienia,k_nazwa,k_adres,k_kod,k_miejsc,k_nip,pr_nazwa,cena_jedn, j_m,
	ilosc,lnetto ,vat,sp_platnosci,termin_platnosci,slownie};
//	pola tekstowe
	public static final JTextField 
	array_nazwa = new JTextField(""),
	txt_netto = new JTextField(""), 
	txt_k_nazwa = new JTextField(""),
	txt_k_adres = new JTextField(""),
	txt_nip = new JTextField(""),
	txt_k_kod = new JTextField(""),
	txt_k_miejsc = new JTextField(""),
	txt_pr_nazwa = new JTextField(""),
	txt_j_m = new JTextField(""),
	txt_cena_jedn = new JTextField(""),	
	txt_ilosc = new JTextField(""),	
	txt_vat = new JTextField(""),	
	txt_sp_platnosci = new JTextField(""),
	txt_termin_platnosci = new JTextField(""),
	txt_data_wystawienia = new JTextField(""),
	txt_nr_f = new JTextField(""),
	txt_slownie = new JTextField("");
	
	
	

//	kontenerek do jtextfield
	public static JTextField[] jtxt_array = { 
			txt_nr_f,txt_data_wystawienia,txt_k_nazwa,txt_k_adres,txt_k_kod,txt_k_miejsc,txt_nip,txt_pr_nazwa,txt_cena_jedn,txt_j_m,
			txt_ilosc,txt_netto ,txt_vat,txt_sp_platnosci,txt_termin_platnosci,
			txt_slownie};
//	wyswietla liste z pojedynczym wyborem
	public static JComboBox cbox = new JComboBox(); 

//	kontener do arraya z jtxtfield
	static HashMap contener_nabywcow = new HashMap();
	
/**
 * tworzy główne okno aplikacji
 *	@param String s - tytul okna
 *
 */
	public Okno(String s) {
		this.setTitle(s);
//		dodaje akcje do przycisków
		Akcje akcje = new Akcje();
		}
	public static void main(String[] args) {
		/* ustawia wygląd (musi być przed stworzeniem JFrame */
		JFrame.setDefaultLookAndFeelDecorated(true);
		Okno ramka = new Okno("Program Fakturka made by Łukasz Kuczera");
		/* zamykanie programu po zamknięciu okna */
		ramka.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		/* dodawanie przycisków do ramki */
		Container cp = ramka.getContentPane();
//		przyciski i opisy z kontenerków
		
		for(int i=0;i < label_array.length; i++) {
			cp.add(label_array[i]);
			jtxt_array[i].setColumns(2);
			cp.add(jtxt_array[i]);
		}

		try {
			ObjectInputStream in = new ObjectInputStream(new FileInputStream("tablica_nabywcow.txt"));
			contener_nabywcow = (HashMap)in.readObject();
			in.close();
			
			// dodaje elementy do listy
			cbox.addItem("wybierz odbiorce");
			String[] t = (String[])contener_nabywcow.keySet().toArray(new String[contener_nabywcow.size()]);
			for(int i=0;i<t.length;i++) 
				cbox.addItem(t[i]);

			cbox.setSelectedItem("wybierz odbiorce");
		}
		catch(IOException E) {
			try {
				cbox.addItem("wybierz odbiorce");
				ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("tablica_nabywcow.txt"));
				out.writeObject(contener_nabywcow);
				out.close();
			}
			catch (IOException e) {
				JOptionPane.showMessageDialog(null,"Wystąpił błąd zapisu: "+e,"Błąd",JOptionPane.ERROR_MESSAGE);
			}
			
		}
		catch(Exception E) {
			JOptionPane.showMessageDialog(null,"Wystąpił błąd: "+E,"Błąd",JOptionPane.ERROR_MESSAGE);
		}



//		dodaje przyciski i liste do panelu zawartosci
		cp.add(generuj);
		cp.add(wyczysc);
		cp.add(zapisz);
		cp.add(zamknij);
		cp.add(cbox);
		cp.add(usun);
		/* menu */
		Menu menu = new Menu();
		menu.updateUI();
		ramka.setJMenuBar(menu);
		
		/* ustawienia wygl�du */
		cp.setLayout(new GridLayout(WINDOW_GRID_ROWS,WINDOW_GRID_COLS));
		ramka.setSize(WINDOW_WIDTH,WINDOW_HEIGHT);
		ramka.setLocation(WINDOW_LOCATION_X,WINDOW_LOCATION_Y);
		
		/* tworzenie i wyswietlanie okienka */
		ramka.pack();
		ramka.setVisible(true);
		

		
	}
		
}

class Menu extends JMenuBar{
	static final long serialVersionUID = 11l;
	private static JMenu confMenu = new JMenu("Opcje");;
	private static JMenuItem mojeDane = new JMenuItem("Moje Dane"),
					zamknij = new JMenuItem("Zamknij"); 
	
	/** 
	 * gotowe menu do dodania 
	 *
	 */
	Menu() {
		
		Menu.zamknij.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		Menu.mojeDane.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Config conf = new Config();
				conf.showOkno();
			}
		});
		confMenu.add(mojeDane);
		confMenu.add(zamknij);
		this.add(confMenu);
		
	}
	/**
	 * Dodaje pozycj� do menu
	 * @param s - string z nazwa kolejnej pozycji w menu
	 * @return item - zwraca obiekt JMenuItem ktory zosta� utworzony 
	 */
	public JMenuItem addItem(String s) {
		JMenuItem item = new JMenuItem(s);
		this.add(item);
		return item;
	}
	
	public class Config {
		private JFrame optRamka = new JFrame();
		private JButton zapiszB, zamknijB; 
		private JLabel
			nazwa = new JLabel("Nazwa"),
			nip = new JLabel("Nip"),
			ulica = new JLabel("Ulica"),
			kod = new JLabel("Kod"),
			miasto = new JLabel("Miasto"),
			nrKonta = new JLabel("Numer Konta"),
			miejsc = new JLabel("<html>Miejscowość<br>wystawienia faktury");
		private Object[] confLabels = {
				nazwa,nip,ulica,kod,miasto,nrKonta,miejsc 
			};
		private JTextField
			txt_nazwa = new JTextField(),
			txt_nip = new JTextField(),
			txt_ulica = new JTextField(),
			txt_kod = new JTextField(),
			txt_miasto = new JTextField(),
			txt_nrKonta = new JTextField(),
			txt_miejsc = new JTextField();
		private JTextField[] confTextFields = {
				txt_nazwa,txt_nip,txt_ulica,txt_kod,
				txt_miasto,txt_nrKonta,txt_miejsc};
		
		private String[] options = { "Zapisz","Anuluj" };
		
		/**
		 * tworzy panel ktory bedzie wyswietlany w nowym oknie konfiguracji
		 * @return JPanel - panel nowego okna
		 */
		
		private JPanel makePane() {
			JPanel jp = new JPanel();
			for(int i=0;i<confLabels.length;i++) {
				jp.add((Component)confLabels[i]);
				jp.add(confTextFields[i]);
			}
			
			zapiszB = new JButton("Zapisz");
			zamknijB = new JButton("Zamknij");
			//akcje
			zapiszB.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						try {
							ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("config_fakturka.txt"));
							out.writeObject(getText(confTextFields));
							out.close();
						}
						catch(Exception E) {
							JOptionPane.showMessageDialog(null,"Wystąpił błąd: "+E,"Błąd",JOptionPane.ERROR_MESSAGE);
						}
					}
				});
			zamknijB.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						optRamka.setVisible(false);
					}
				});
			jp.add(zapiszB);
			jp.add(zamknijB);
			jp.setLayout(new GridLayout(8,2));
			loadConfig();
			return jp;
		}
		/**
		 * ustawia JTextField zgodnie z zapisaną wcześniej konfiguracją
		 *
		 */
		private void loadConfig() {
			
			try {
				ObjectInputStream in = new ObjectInputStream(new FileInputStream("config_fakturka.txt"));
				String[] txt_array = (String[])in.readObject();
				for(int i=0;i<txt_array.length;i++)
					confTextFields[i].setText(txt_array[i]);
			}
			catch(IOException E) {
				E.printStackTrace();
				JOptionPane.showMessageDialog(null,"Wpisz swoje dane i naciśnij \"Zapisz\"","Wpisz dane",JOptionPane.INFORMATION_MESSAGE);
			}
			catch(Exception E) {
				JOptionPane.showMessageDialog(null,"Wystąpił błąd: "+E,"Błąd",JOptionPane.ERROR_MESSAGE);
			}
			
		}
		/**
		 * zwraca tablice textow wpisanych w pola
		 * @param txtArray - tablica pol JTextField
		 * @return - tablica String[] zawieraj�ca texty wprowadzone w pola txtArray
		 */
		private String[] getText(JTextField[] txtArray) {
			String[] result = new String[txtArray.length];
			for(int i=0;i<txtArray.length;i++) {
				result[i] = txtArray[i].getText();
			}
			return result;
		}
		
		/**
		 * tworzy nowe okno z wiadomoscia
		 * @param body - cia�o okna - jpanel, dialog itp.
		 * @pram title - tytul nowego okienka
		 *
		 */
		private void makeOkno(Container body, String title) {
			optRamka.setTitle(title);
			Container cp = optRamka.getContentPane();
			cp.add(body);
			//ustawia na srodku ekranu
			optRamka.setSize(300,300);
			optRamka.setLocation(300,200);
			optRamka.pack();
			optRamka.setVisible(true);
		}
		/**
		 * wyświetla okno
		 */
		public void showOkno() {
			makeOkno(makePane(),"Dane sprzedającego");
		}
		
	}
}



