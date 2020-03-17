import java.io.IOException;

public class Controller {
    private ConsoleView view;

    public Controller() {
        view = new ConsoleView();
        view.addWidget(new FloatInputWidget("operation_input1", "Operatior 1: ", 1, 1));
        view.addWidget(new FloatInputWidget("operation_input2", "Operatior 2: ", 1, 2));
        view.addWidget(new OperatorInputWidget("operator", "Opertator: ", 1, 4));
        view.addWidget(new TextWidget("result_text", 1, 7) {
            public void onUpdate(int key) {
                String op1 = root.getByName("operation_input1").getValue();
                String op2 = root.getByName("operation_input2").getValue();
                String oper = root.getByName("operator").getValue();
                try {
                    switch (oper.charAt(0)) {
                        case '+':
                            content = Float.toString(Float.parseFloat(op1) + Float.parseFloat(op2));
                            break;
                        case '-':
                            content = Float.toString(Float.parseFloat(op1) - Float.parseFloat(op2));
                            break;
                        case '*':
                            content = Float.toString(Float.parseFloat(op1) * Float.parseFloat(op2));
                            break;
                        case '/':
                            content = Float.toString(Float.parseFloat(op1) / Float.parseFloat(op2));
                            break;
                        default:
                            
                    }
                } catch (NumberFormatException | StringIndexOutOfBoundsException e) {
                    content = "";
                }
            }
        });
    }

    public void start() {
        ConsoleUtils.set_raw_mode();
        try {
            int key;
            view.onDraw();
            while ((key = System.in.read()) != 4) {
                view.onUpdate(key);
                view.onDraw();
            }
            view.onCleanUp();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}