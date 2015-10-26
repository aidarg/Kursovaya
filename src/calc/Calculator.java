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
// Содержимое панели с ошибками
		// Установаить внешний вид теста в сообщени об ошибке
		StyleConstants.setAlignment(attribs, StyleConstants.ALIGN_CENTER);
		StyleConstants.setFontFamily(attribs, "Sans-Serif");
		StyleConstants.setBold(attribs, true);
		lError.setPreferredSize(new Dimension(200, 80));
		lError.setParagraphAttributes(attribs,true);
		// Установить цвет фона
		lError.setBackground(frame.getBackground());

		// Содержимое последней панели с кнопкой "Рассчитать"
		JButton bCalculate = new JButton("Рассчитать");

		// Обработчики событий
		jNumberOfPeople.getDocument().addDocumentListener(onFillListener);
		jCostOneDay.getDocument().addDocumentListener(onFillListener);
		jDays.getDocument().addDocumentListener(onFillListener);
		bCalculate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (!validate() || !validateFilled()) {
					return;
				}
				costOneDay = Double.parseDouble(jCostOneDay.getText());
				days = Integer.parseInt(jDays.getText());
				numberOfPeople = Integer.parseInt(jNumberOfPeople.getText());
				for (int i=0; i<numberOfPeople; i++) {
					personalAviaCost[i] = Double.parseDouble(jPersonalAviaCost[i].getText());
					personalTransferCost[i] = Double.parseDouble(jPersonalTransferCost[i].getText());
				}
				showAnswer(""+calculate());
			}
		});

		pTableColsHeadings.add(lAvia);
		lAvia.setHorizontalAlignment(SwingConstants.CENTER);
		pTableColsHeadings.add(lTransfer);
		lTransfer.setHorizontalAlignment(SwingConstants.CENTER);

		frame.setContentPane(pMain);
		frame.setResizable(false);
		// Собираем панели из элементов
		// Первая
		pFirst.add(lCostOneDay);
		pFirst.add(jCostOneDay);
		pFirst.add(lDays);
		pFirst.add(jDays);
		pFirst.add(lNumberOfPeople);
		pFirst.add(jNumberOfPeople);
		// Заголовок всей таблицы
		pTableHeader.add(lTourists);
		// Заголовки колонок таблицы
		pTableColsHeadings.add(lAvia);
		pTableColsHeadings.add(lTransfer);
		// Ошибки
		pError.add(lError);
		// Кнопки
		pButtons.add(bCalculate);
		// Собираем все панели на главной панели
		pMain.add(pFirst);
		pMain.add(pTableHeader);
		pMain.add(pTableColsHeadings);
		pMain.add(pTable);
		pMain.add(pError);
		pMain.add(pButtons);

		frame.pack();
		frame.setVisible(true);
	}
	// Возвращает true, если все поля формы валидные. Иначе возвращает false.
	// Форма считается валидной, если в каждом из полей текст ещё не введён
	// или введён текст, корректно преобразующийся к нужному числовому типу
	// данных.