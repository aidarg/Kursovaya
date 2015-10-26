package calc;
import javax.swing.*;
import javax.swing.text.*;
import javax.swing.event.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
public class Calculator {
	private static final int MAX_PEOPLE = 100;
	private double costOneDay;            // Сс
	private int days;                     // H
	private int numberOfPeople = 0;       // n
	private double[] personalAviaCost = new double[MAX_PEOPLE];    // Can
	private double[] personalTransferCost = new double[MAX_PEOPLE];// Синд
	private JTextField jCostOneDay = new JTextField();
	private JTextField jDays = new JTextField();
	private JTextField jNumberOfPeople = new JTextField();
	private JTextField[] jPersonalAviaCost = new JTextField[MAX_PEOPLE];
	private JTextField[] jPersonalTransferCost = new JTextField[MAX_PEOPLE];
	// Для отображения ошибок
	private final JTextPane lError = new JTextPane(); 
	// Таблица для ввода стоимостей трансфера и авиаперелёта для
	// отдельных пассажиров.
	private final JPanel pTable = new JPanel(new GridLayout(0, 2, 4, 4));
	private final JFrame frame = new JFrame("Рассчёт стоимости тура");
	// Визуальные свойства текста
	SimpleAttributeSet attribs = new SimpleAttributeSet();
	private final DocumentListener onFillListener = new DocumentListener() {
		public void changedUpdate(DocumentEvent e) {
			process();
		}
		public void removeUpdate(DocumentEvent e) {
			process();
		}
		public void insertUpdate(DocumentEvent e) {
			process();
		}
		public void process() {
		// Сперва проверяем всю форму.
			if (jNumberOfPeople.getText().equals("") || !validate()) {
				return;
			}
			int prevNumberOfPeople = numberOfPeople;
			numberOfPeople = Integer.parseInt(jNumberOfPeople.getText());
			buildTouristsTable(prevNumberOfPeople);
		}
	};
	public static void main(String[] args) {
		new Calculator();
	}
	public Calculator() {
		// Создаём окно приложения
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		// Создаём внутренности окна
		JPanel pMain = new JPanel();
		
		JPanel pFirst = new JPanel();
		pFirst.setPreferredSize(new Dimension(300,110));
		pFirst.setLayout(new BoxLayout(pFirst, BoxLayout.Y_AXIS));
		JPanel pTableColsHeadings = new JPanel(new GridLayout(0, 2));
		JPanel pButtons = new JPanel(new GridLayout(0, 2));
		JPanel pTableHeader = new JPanel();
		JPanel pError = new JPanel();

		//panel.setLayout(new GridLayout(0, 1));
		pMain.setLayout(new BoxLayout(pMain, BoxLayout.Y_AXIS));
		pMain.setBorder(new EmptyBorder(10, 10, 10, 10));

		// Содержимое первой панели
		JLabel lCostOneDay = new JLabel("Стоимость одного дня проживания");
		JLabel lDays = new JLabel("Количество дней проживания");
		JLabel lNumberOfPeople = new JLabel("Количество человек");

		// Содержимое второй панели
		JLabel lTourists = new JLabel("Индивидуальная стоимость");
		// Содержимое третьей панели
		JLabel lAvia = new JLabel("Перелёта");
		JLabel lTransfer = new JLabel("Трансфера");