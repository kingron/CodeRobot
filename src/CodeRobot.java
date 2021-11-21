import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.editor.Caret;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;

/**
 * CodeRobot插件接口
 */
public class CodeRobot extends AnAction {
    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        Project project = e.getData(PlatformDataKeys.PROJECT);

        Editor editor = e.getRequiredData(CommonDataKeys.EDITOR);
        Document document = editor.getDocument();
        Caret primaryCaret = editor.getCaretModel().getPrimaryCaret();

        DlgEditor dlgEditor = new DlgEditor(e.getProject());
        String template = primaryCaret.getSelectedText();
        dlgEditor.setTemplate(template);
        if (!dlgEditor.showAndGet()) return;

        String ret = dlgEditor.getResult();
        int endPos = primaryCaret.getSelectionEnd();
        WriteCommandAction.runWriteCommandAction(project, () -> document.insertString(endPos, ret));
    }
}
