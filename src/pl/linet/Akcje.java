package pl.linet;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.*;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
/*
 * Created on 2005-12-09
 *
 * 
 */

/**
 * @author kukems
 *
 * 
 */
public class Akcje {
	HashMap contener_nabywcow = new HashMap();
	Akcje() {
		Okno.wyczysc.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
	//			petla zaczyna sie od jeden bo pierwszy jtxtfield nadaje nazwe
	//			calej tablicy i musi byc zachowana
				for(int i=0;i<Okno.jtxt_array.length;i++) {
					Okno.jtxt_array[i].setText("");
				}
			}
		});
		Okno.zapisz.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String nazwa_nabywcy = JOptionPane.showInputDialog(null,"Wpisz nazwę nabywcy","Zapisywanie danych", JOptionPane.PLAIN_MESSAGE);
				zapiszDane(nazwa_nabywcy);
			}
		});
		
		Okno.zamknij.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e){
				System.exit(0);
			}
		});
		Okno.cbox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JComboBox t = (JComboBox)e.getSource();
				if(t.getSelectedItem()!="wybierz odbiorce")
					wczytajDane((String)t.getSelectedItem());
			}
		});
		Okno.usun.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				usunDane((String)Okno.cbox.getSelectedItem());
			}
		});
		Okno.generuj.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Generuj pdf = new Generuj();
				try {
					pdf.generatePdf(Okno.txt_k_nazwa.getText(),Okno.txt_k_adres.getText(),Okno.txt_nip.getText(),Okno.txt_k_kod.getText(),Okno.txt_k_miejsc.getText(),
							Okno.txt_pr_nazwa.getText(),Okno.txt_pkwiu.getText(),Okno.txt_ilosc.getText(),
							Float.valueOf(Okno.txt_netto.getText()).floatValue(),
							Integer.parseInt(Okno.txt_vat.getText()),
							Okno.txt_sp_platnosci.getText(),Okno.txt_termin_platnosci.getText(),
							Okno.txt_nr_f.getText(),Okno.txt_data_wystawienia.getText(),
							Okno.txt_slownie.getText()
					);
					try {
						Runtime.getRuntime().exec("explorer faktura.pdf");
					} catch (IOException e1) {
						JOptionPane.showMessageDialog(null, e1.getMessage());
						e1.printStackTrace();
					}
			}
			catch (NumberFormatException nfE) {
				JOptionPane.showMessageDialog(null,
				"Wpisano źle dane liczbowe: "+nfE+"." +
				"\n Odpowiedni format np. netto: 22.34 vat: 22","Błąd",JOptionPane.ERROR_MESSAGE);
			}
			catch(RuntimeException ex) {
				JOptionPane.showMessageDialog(null, "Problem z aplikacja");
				throw ex;
			}
		}
	});
	}
	//	koniec akcji	
	
	private void zapiszDane(String nazwa) {
		try {
//			wczytuje obiekt z pliku ktory jezeli nie istnieje to jest tworzony podczas startu programu
			ObjectInputStream in = new ObjectInputStream(new FileInputStream("tablica_nabywcow.txt"));
			contener_nabywcow = (HashMap)in.readObject();
			in.close();
//			dodaje nowe pola i zapisuje spowrotem
			if(contener_nabywcow.containsKey(nazwa))
				throw new MyException();
			Okno.cbox.addItem(nazwa);
			contener_nabywcow.put(nazwa, Okno.jtxt_array);
			ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("tablica_nabywcow.txt"));
			out.writeObject(contener_nabywcow);
			out.close();
		}
		catch(MyException E) {
			JOptionPane.showMessageDialog(null,"Wystąpił błąd: podana nazwa już istnieje","Podaj inną nazwę",JOptionPane.WARNING_MESSAGE);
		}
		catch(ClassCastException E) {
			JOptionPane.showMessageDialog(null,"Wystąpił błąd: "+E,"Błąd",JOptionPane.ERROR_MESSAGE);
		}
		catch(Exception ioE) {
			JOptionPane.showMessageDialog(null,"Wystąpił błąd: podczas odczytu/zapisu pliku: "+ioE,"Błąd", JOptionPane.ERROR_MESSAGE);
		}
		
	}
	/**
	 *  
	 */ 
	private void usunDane(String klucz){
		try {
			ObjectInputStream in = new ObjectInputStream(new FileInputStream("tablica_nabywcow.txt"));
			contener_nabywcow = (HashMap)in.readObject();
			in.close();
			contener_nabywcow.remove(klucz);
			ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("tablica_nabywcow.txt"));
			out.writeObject(contener_nabywcow);
			out.close();
			Okno.cbox.removeItem(klucz);
		}
		catch(ClassCastException E) {
			JOptionPane.showMessageDialog(null,"Wystąpił błąd:  "+E,"Błąd",JOptionPane.ERROR_MESSAGE);
		}
		catch(IOException ioE) {
			JOptionPane.showMessageDialog(null,"Wystąpił błąd podczas odczytu/zapisu pliku: "+ioE,"Błąd", JOptionPane.ERROR_MESSAGE);
		}
		catch(ClassNotFoundException E) {
			JOptionPane.showMessageDialog(null,"Błąd w strukturze pliku tablica_nabywcow.txt usuń ten plik i uruchom ponownie program UWAGA utracisz dane odbiorców."+E,"Błąd", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	private void wczytajDane(String klucz) {
		try {
			ObjectInputStream in = new ObjectInputStream(new FileInputStream("tablica_nabywcow.txt"));
			contener_nabywcow = (HashMap)in.readObject();
			String[] t = (String[])contener_nabywcow.keySet().toArray(new String[contener_nabywcow.size()]);
			JTextField[] tmp_array = (JTextField[])contener_nabywcow.get(klucz);
			for(int i=0;i<tmp_array.length;i++) {
				Okno.jtxt_array[i].setText(tmp_array[i].getText());
			}
		}
		catch(Exception E) {
			JOptionPane.showMessageDialog(null,"Wystąpił błąd:  "+E,"Błąd",JOptionPane.ERROR_MESSAGE);
		}
	}
	
	private void zapisz() {
		try {
			ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("tablica_nabywcow.txt"));
			out.writeObject(contener_nabywcow);
			out.close();
		}
		catch(Exception E) {
			JOptionPane.showMessageDialog(null,"Wystąpił błąd: "+E,"Błąd",JOptionPane.ERROR_MESSAGE);
		}
	}
	class MyException extends Exception {
		static final long serialVersionUID = 15l; 
	}
}