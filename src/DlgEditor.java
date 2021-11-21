import com.intellij.ide.util.PropertiesComponent;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.ui.components.JBScrollPane;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;

/**
 * CodeRobot 主窗体
 *
 * @author Kingron
 */
public class DlgEditor extends DialogWrapper {
    private final static String S_TEMPLATE = "template";
    private final static String S_PARAMS = "params";

    private JTextArea txtTemplate;
    private JTextArea txtParams;
    private PropertiesComponent pc = PropertiesComponent.getInstance();

    protected DlgEditor(@Nullable Project project) {
        super(project);
        init();
        setTitle("Code Robot");
        loadSettings();
    }

    @Override
    protected @Nullable
    JComponent createCenterPanel() {
        JPanel panel = new JPanel();
        Insets insets = new Insets(8, 8, 8, 8);
        Font font = new Font(Font.DIALOG_INPUT, Font.PLAIN, 12);
        GridBagLayout gridBagLayout = new GridBagLayout();

        panel.setLayout(gridBagLayout);
        panel.setFont(font);
        panel.setPreferredSize(new Dimension(800, 600));

        JLabel lblTemplate = new JLabel("<html>Template code, place holder supported: <b>#1, #2, #3</b>…… etc, Place holder can be reused. Example:<br/>" +
                "inputMap.put(\"#1.#2\", String.valueOf(input.#1.#2));\t// #3</html>");
        panel.add(lblTemplate);

        txtTemplate = new JTextArea();
        txtTemplate.setFont(font);
        txtTemplate.setMargin(insets);
        JScrollPane sbTemplate = new JBScrollPane(txtTemplate);
        panel.add(sbTemplate);

        JLabel lblParam = new JLabel("<html>Parameters, each line will generate a copy of the template, use TAB char to separator param in a line. Example:<br/>" +
                "com1\tkey1\tBla bla 1");
        panel.add(lblParam);

        txtParams = new JTextArea();
        txtParams.setFont(font);
        txtParams.setMargin(insets);
        JScrollPane sbParam = new JBScrollPane(txtParams);
        panel.add(sbParam);

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.BOTH;
        constraints.gridwidth = 0;
        constraints.gridheight = 1;
        constraints.weightx = 1;

        constraints.weighty = 1;
        gridBagLayout.setConstraints(lblTemplate, constraints);

        constraints.weighty = 2;
        gridBagLayout.setConstraints(sbTemplate, constraints);

        constraints.weighty = 1;
        gridBagLayout.setConstraints(lblParam, constraints);

        constraints.weighty = 20;
        gridBagLayout.setConstraints(sbParam, constraints);

        return panel;
    }

    /**
     * 设置默认模板，不设置的话，默认为保存的参数
     *
     * @param template the template string
     */
    public void setTemplate(String template) {
        if (template == null || template.trim().equals("")) return;

        txtTemplate.setText(template);
    }

    /**
     * 点击OK时保存配置
     */
    @Override
    protected void doOKAction() {
        saveSettings();
        super.doOKAction();
    }

    /**
     * 返回生成的代码
     *
     * @return 生成的代码
     */
    public String getResult() {
        return makeCode(txtTemplate.getText().trim(), txtParams.getText().trim());
    }

    /**
     * 生成雷同代码的函数，核心函数
     *
     * @param template 代码模板字符串
     * @param params   参数，多行分隔，每一行的参数用 TAB 分隔
     * @return 生成的代码
     */
    private String makeCode(String template, String params) {
        StringBuilder sb = new StringBuilder();

        String[] ss = params.split("\n");
        for (String line : ss) {
            String ss2[] = line.split("\t");
            String resultLine = template;

            for (int i = 0; i < ss2.length; i++) {
                resultLine = resultLine.replace("#" + (i + 1), ss2[i].trim());
            }
            sb.append(resultLine + "\n");
        }
        return sb.toString();
    }

    /**
     * 加载配置
     */
    private void loadSettings() {
        txtParams.setText(pc.getValue(S_PARAMS));
        txtTemplate.setText(pc.getValue(S_TEMPLATE));
    }

    /**
     * 保存配置
     */
    private void saveSettings() {
        pc.setValue(S_PARAMS, txtParams.getText());
        pc.setValue(S_TEMPLATE, txtTemplate.getText());
    }
}
