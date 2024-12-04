import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.NumberFormat;
import java.util.Locale;

public class WaterBillingGUI {
    public static void main(String[] args) {
        // Create the main frame
        JFrame frame = new JFrame("Water Billing System");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 500);
        frame.setLayout(new BorderLayout(10, 10));

        // Title
        // JLabel titleLabel = new JLabel("Water Billing System", JLabel.CENTER);
        // titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        // frame.add(titleLabel);

        // Input panel
        JPanel inputPanel = new JPanel(new GridBagLayout());
        inputPanel.setBorder(BorderFactory.createTitledBorder("Water Billing System | Input Section"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Input: Water Usage
        gbc.gridx = 0;
        gbc.gridy = 0;
        JLabel usageLabel = new JLabel("Water Used (litres):");
        inputPanel.add(usageLabel, gbc);

        gbc.gridx = 1;
        JTextField usageField = new JTextField(100);
        inputPanel.add(usageField, gbc);

        // Input: Season
        gbc.gridx = 0;
        gbc.gridy = 1;
        JLabel seasonLabel = new JLabel("Select Season:");
        inputPanel.add(seasonLabel, gbc);

        gbc.gridx = 1;
        String[] seasons = {"Summer", "Autumn", "Winter", "Spring"};
        JComboBox<String> seasonDropdown = new JComboBox<>(seasons);
        inputPanel.add(seasonDropdown, gbc);

        //Input: Month
        gbc.gridx = 0;
        gbc.gridy = 2;
        JLabel monthLabel = new JLabel("Select Month:");
        inputPanel.add(monthLabel, gbc);

        gbc.gridx = 1;
        String[] months = {"January", "Febuary", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
        JComboBox<String> monthDropdown = new JComboBox<>(months);
        inputPanel.add(monthDropdown, gbc);

        frame.add(inputPanel, BorderLayout.NORTH);  //input panel borderlayout

        // Buttons panel
        JPanel buttonPanel = new JPanel();
        JButton calculateButton = new JButton("Calculate Water Bill");
        JButton resetButton = new JButton("Reset");
        buttonPanel.add(calculateButton);
        buttonPanel.add(resetButton);
        frame.add(buttonPanel, BorderLayout.SOUTH);

        // Output panel
        JPanel outputPanel = new JPanel(new BorderLayout());
        outputPanel.setBorder(BorderFactory.createTitledBorder("Water Billing System | Output Section"));

        JTextArea outputArea = new JTextArea(10, 40);
        outputArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
        outputArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(outputArea);
        outputPanel.add(scrollPane, BorderLayout.CENTER);

        frame.add(outputPanel, BorderLayout.CENTER);

        // Action Listeners
        calculateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    Double litersUsed = Double.parseDouble(usageField.getText());
                    String season = (String) seasonDropdown.getSelectedItem();
                    String month = (String) monthDropdown.getSelectedItem();

                    if (litersUsed < 0) {
                        throw new NumberFormatException("Negative value entered");
                    }

                    // Call the appropriate calculation method based on the season
                    String result;
                    switch (season) {
                        case "Summer":
                            result = CalcSummerWaterBill(litersUsed, month);
                            break;
                        case "Autumn":
                            result = CalcAutumnWaterBill(litersUsed, month);
                            break;
                        case "Winter":
                            result = CalcWinterWaterBill(litersUsed, month);
                            break;
                        case "Spring":
                            result = CalcSpringWaterBill(litersUsed, month);
                            break;
                        default:
                            result = "Invalid season selected.";
                            break;
                    }
                    outputArea.setText(result);
                } catch (NumberFormatException ex) {
                    outputArea.setText("Invalid input. Please enter a positive number for water usage.");
                }
            }
        });
        //reset funtionality 
        resetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                usageField.setText("");
                seasonDropdown.setSelectedIndex(-1);
                monthDropdown.setSelectedIndex(-1);
                outputArea.setText("");
            }
        });

        // Display the frame
        frame.setVisible(true);
    }

    // -------------------------- Logic section -------------------------- //
    public static class VAT {
        Double VATPercentage = 0.15;
    }

    public static String CalcSummerWaterBill(Double LitresUsed, String Month) {
        String SeasonName = "Summer";
        Integer SummerWaterThreshold = 7000;
        Double UnderCostPerLitre = 2.78;
        Double OverCostPerLitre = 0.62;
        Double DiscountPercentage = 0.12;
        Double BasicPayment = 38.00;

        Double Subtotal;
        Double WaterBill;
        Double FinalWaterBill=0.00; //with discount
        Double CostExtraLitres;
        Double CostThresholdLitres;
        Boolean IsOverThreshold = false;

        VAT VTax = new VAT();

        if (LitresUsed > SummerWaterThreshold) {
            IsOverThreshold = true;
            CostExtraLitres = (LitresUsed - SummerWaterThreshold) * OverCostPerLitre;
            CostThresholdLitres = SummerWaterThreshold * UnderCostPerLitre;
            Subtotal = CostExtraLitres + CostThresholdLitres + BasicPayment;
            WaterBill = (Subtotal * VTax.VATPercentage) + Subtotal;
            return PrintFinalWaterBill(WaterBill, IsOverThreshold, SeasonName, LitresUsed, CostExtraLitres, SummerWaterThreshold, Month, CostThresholdLitres, DiscountPercentage);
        } else {
            CostThresholdLitres = (LitresUsed*UnderCostPerLitre);
            Subtotal = CostThresholdLitres + BasicPayment;
            WaterBill = (Subtotal*VTax.VATPercentage)+Subtotal;
            FinalWaterBill =WaterBill - (WaterBill*DiscountPercentage);
            return PrintFinalWaterBill(FinalWaterBill, IsOverThreshold, SeasonName, LitresUsed, 0.0, SummerWaterThreshold, Month, CostThresholdLitres, DiscountPercentage);
        }
    }

    public static String CalcAutumnWaterBill(Double LitresUsed, String Month) {
        String SeasonName = "Autumn";
        Integer AutumnWaterThreshold = 5000;
        Double UnderCostPerLitre = 2.65; //under threshold cost per litre
        Double OverCostPerLitre = 0.50; //cost per litre over the threshold
        Double DiscountPercentage = 0.20; //discount percent on final water bill
        Double BasicPayment = 38.00;

        Double Subtotal; //cost of water used
        Double WaterBill=0.00; //cost of water used plus VAT
        Double FinalWaterBill=0.00; //with discount
        Double CostExtraLitres;
        Double CostThresholdLitres;
        Boolean IsOverThreshold = false;

        VAT VTax = new VAT();   //creating a new VAT Object

        //if customer has used water above the threshold
        if(LitresUsed > AutumnWaterThreshold){
            IsOverThreshold = true;
            CostExtraLitres = (LitresUsed-AutumnWaterThreshold)*OverCostPerLitre;
            CostThresholdLitres = AutumnWaterThreshold*UnderCostPerLitre;
            Subtotal = CostExtraLitres + CostThresholdLitres + BasicPayment;
            WaterBill = (Subtotal*VTax.VATPercentage)+Subtotal;
            return PrintFinalWaterBill(WaterBill, IsOverThreshold, SeasonName, LitresUsed, CostExtraLitres, AutumnWaterThreshold, Month, CostThresholdLitres, DiscountPercentage);
        }
        //if customer has used water within the threshold
        else{
            CostThresholdLitres = (LitresUsed*UnderCostPerLitre);
            Subtotal = CostThresholdLitres + BasicPayment;
            WaterBill = (Subtotal*VTax.VATPercentage)+Subtotal;
            FinalWaterBill =WaterBill - (WaterBill*DiscountPercentage);
            return PrintFinalWaterBill(FinalWaterBill, IsOverThreshold, SeasonName, LitresUsed, 0.0, AutumnWaterThreshold, Month, CostThresholdLitres, DiscountPercentage);
        }
    }

    public static String CalcWinterWaterBill(Double LitresUsed, String Month) {
        String SeasonName = "Winter";
        Integer WinterWaterThreshold = 5000;
        Double CostPerLitre = 2.65; //under threshold cost per litre
        Double BasicPayment = 38.00;
        Boolean IsOverThreshold = false;

        Double CostThresholdLitres;
        Double Subtotal; //cost of water used
        // Double WaterBill=0.00; //cost of water used plus VAT

        VAT VTax = new VAT();   //creating a new VAT Object
        if(LitresUsed > WinterWaterThreshold){
            IsOverThreshold = true;
        }
        
        CostThresholdLitres = (LitresUsed*CostPerLitre) + BasicPayment;
        Subtotal = CostThresholdLitres + (CostThresholdLitres*VTax.VATPercentage);
        return PrintFinalWaterBill(Subtotal, IsOverThreshold, SeasonName, LitresUsed, 0.0, WinterWaterThreshold, Month, CostThresholdLitres, 0.00);
    }

    public static String CalcSpringWaterBill(Double LitresUsed, String Month) {
        String SeasonName = "Spring";
        Integer SpringWaterThreshold = 5000;
        Double UnderCostPerLitre = 2.65; //under threshold cost per litre
        Double OverCostPerLitre = 0.50; //cost per litre over the threshold
        Double DiscountPercentage = 0.20; //discount on final water bill
        Double BasicPayment = 38.00;

        Double Subtotal; //cost of water used
        Double WaterBill=0.00; //cost of water used plus VAT
        Double FinalWaterBill=0.00; //with discount
        Double CostExtraLitres;
        Double CostThresholdLitres;
        Boolean IsOverThreshold = false;

        VAT VTax = new VAT();   //creating a new VAT Object

        //if customer has used water above the threshold
        if(LitresUsed > SpringWaterThreshold){
            IsOverThreshold = true;
            CostExtraLitres = (LitresUsed-SpringWaterThreshold)*OverCostPerLitre;
            CostThresholdLitres = SpringWaterThreshold*UnderCostPerLitre;
            Subtotal = CostExtraLitres + CostThresholdLitres + BasicPayment;
            WaterBill = (Subtotal*VTax.VATPercentage)+Subtotal;
            return PrintFinalWaterBill(WaterBill, IsOverThreshold, SeasonName, LitresUsed, CostExtraLitres, SpringWaterThreshold, Month, CostThresholdLitres, DiscountPercentage);
        }
        //if customer has used water within the threshold
        else{
            CostThresholdLitres = (LitresUsed*UnderCostPerLitre);
            Subtotal = CostThresholdLitres + BasicPayment;
            WaterBill = (Subtotal*VTax.VATPercentage)+Subtotal;
            FinalWaterBill =WaterBill - (WaterBill*DiscountPercentage);
            return PrintFinalWaterBill(FinalWaterBill, IsOverThreshold, SeasonName, LitresUsed, 0.0, SpringWaterThreshold, Month, CostThresholdLitres, DiscountPercentage);
        }
    }

    public static String PrintFinalWaterBill(Double TotalWaterBill, 
        Boolean OverSetThreshold, 
        String Season, 
        Double WaterUsed, 
        Double CostExtraLitres, 
        Integer SeasonThreshold,
        String UMonth,
        Double CostLitresUsedThreshold, 
        Double DiscountPercentageApplied) 
        {
        String newline = System.lineSeparator();
        NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(new Locale("en", "ZA"));

        if(Season.equals("Winter" )){
            if(OverSetThreshold){
                return "-------------------- Your Water Bill Details: --------------------" + newline +
                    "   Month: " + UMonth + newline +
                    "   Season: " + Season + newline +
                    "   Season Threshold: " + SeasonThreshold + " Litres" + newline + newline +
                    "   Water Consumed: " + WaterUsed + " Litres" + newline +

                    "   Cost of Litres used: " + currencyFormatter.format(CostLitresUsedThreshold) + newline +
                    "   Basic Payment fee: " + currencyFormatter.format(38) + newline + newline +
                    "   Subtotal: " + currencyFormatter.format((CostLitresUsedThreshold+38)) + newline +
                    "   Value Added Tax at 15%: " + currencyFormatter.format((CostLitresUsedThreshold+38)*0.15) + newline +newline +
                    "   Total Water Bill: " + currencyFormatter.format(TotalWaterBill) + newline + newline +

                    "   Message: You have exceeded the threshold" + newline +
                    "------------------------------- Thank You ---------------------------";
            } else {
                return "-------------------- Your Water Bill Details: --------------------" + newline +
                    "   Month: " + UMonth + newline +
                    "   Season: " + Season + newline +
                    "   Season Threshold: " + SeasonThreshold + " Litres" + newline + newline +
                    "   Water Consumed: " + WaterUsed + " Litres" + newline +

                    "   Cost of Litres used: " + currencyFormatter.format(CostLitresUsedThreshold) + newline +
                    "   Basic Payment fee: " + currencyFormatter.format(38) + newline + newline +
                    "   Subtotal: " + currencyFormatter.format((CostLitresUsedThreshold+38)) + newline +
                    "   Value Added Tax at 15%: " + currencyFormatter.format((CostLitresUsedThreshold+38)*0.15) + newline +newline +
                    "   Total Water Bill: " + currencyFormatter.format(TotalWaterBill) + newline + newline +

                    "   Message: You have not exceeded the threshold" + newline +
                    "------------------------------- Thank You ---------------------------";
            } 
        }  else if (OverSetThreshold) {
            return "-------------------- Your Water Bill Details: --------------------" + newline +
                    "   Month: " + UMonth + newline +
                    "   Season: " + Season + newline +
                    "   Season Threshold: " + SeasonThreshold + " Litres" + newline + newline +
                    "   Water Consumed: " + WaterUsed + " Litres" + newline +

                    "   Cost of Extra Litres: " + currencyFormatter.format(CostExtraLitres) + newline +
                    "   Cost of Litres used within Threshold: " + currencyFormatter.format(CostLitresUsedThreshold) + newline +
                    "   Basic Payment fee: " + currencyFormatter.format(38) + newline + newline +
                    "   Subtotal: " + currencyFormatter.format((CostExtraLitres+CostLitresUsedThreshold+38)) + newline +
                    "   Value Added Tax at 15%: " + currencyFormatter.format((CostExtraLitres+CostLitresUsedThreshold+38)*0.15) + newline +newline +
                    "   Total Water Bill: " + currencyFormatter.format(TotalWaterBill) + newline + newline +
                    
                    "   Message: You have exceeded threshold by " + Math.round((WaterUsed - SeasonThreshold)*100.0)/100.0 + " litres." + newline +
                    "------------------------------- Thank You -------------------------";
        } else {
            return "-------------------- Your Water Bill Details: --------------------" + newline +
                    "   Month: " + UMonth + newline +
                    "   Season: " + Season + newline +
                    "   Season Threshold: " + SeasonThreshold + " Litres" + newline + newline +
                    "   Water Consumed: " + WaterUsed + " Litres" + newline +

                    "   Cost of Litres used within Threshold: " + currencyFormatter.format(CostLitresUsedThreshold) + newline +
                    "   Basic Payment fee: " + currencyFormatter.format(38) + newline + newline +
                    "   Subtotal: " + currencyFormatter.format((CostLitresUsedThreshold+38)) + newline +
                    "   Value Added Tax at 15%: " + currencyFormatter.format((CostLitresUsedThreshold+38)*0.15) + newline +
                    "   Discount Amount: " + currencyFormatter.format(((CostLitresUsedThreshold+38)*0.15+(CostLitresUsedThreshold+38))*DiscountPercentageApplied) + newline + newline +
                    "   Total Water Bill: " + currencyFormatter.format(TotalWaterBill) + newline + newline +

                    "   Message: Discount applied for staying within the threshold." + newline +
                    "------------------------------- Thank You ------------------------------";
        }
    }
}
