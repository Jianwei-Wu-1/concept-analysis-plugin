package PluginMain;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManager;
import edu.udel.RunAnalysisKt;

public class RunConceptAnalysis_main extends AnAction {

    @Override
    public void actionPerformed(AnActionEvent e) {

        for (Project project : ProjectManager.getInstance().getOpenProjects()) {

            Project[] args = new Project[]{project};
            RunAnalysisKt.main(args);
        }

        System.exit(0);
    }
}
