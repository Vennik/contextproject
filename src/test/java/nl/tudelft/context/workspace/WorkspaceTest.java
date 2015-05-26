package nl.tudelft.context.workspace;

import junit.framework.TestCase;
import org.junit.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import static junit.framework.TestCase.assertEquals;

/**
 * @author Gerben Oolbekkink <g.j.w.oolbekkink@gmail.com>
 * @version 1.1
 * @since 9-5-2015
 */
public class WorkspaceTest {

    @Test
    public void testNullDirectory() throws Exception {
        Workspace workspace = new Workspace(null);

        assertEquals(null, workspace.directory);
    }

    @Test
    public void testDirectory() throws Exception {
        File file = new File("mydir");
        Workspace workspace = new Workspace(file);

        assertEquals(file, workspace.directory);
    }

    @Test
    public void testOneGraph() throws Exception {
        Workspace workspace = new Workspace(null);
        File[] pathList = new File[5];

        File edgePath = new File("mygraph.edge.graph");
        File nodePath = new File("mygraph.node.graph");
        File nwkPath = new File("mygraph.nwk");
        File annPath = new File("mygraph.ann.csv");
        File immPath = new File("mygraph.imm.csv");

        pathList[0] = edgePath;
        pathList[1] = nodePath;
        pathList[2] = nwkPath;
        pathList[3] = annPath;
        pathList[4] = immPath;

        workspace.files = pathList;

        workspace.load();

        assertEquals(nodePath, workspace.getNodeFile());
        assertEquals(edgePath, workspace.getEdgeFile());
        assertEquals(nwkPath, workspace.getNwkFile());
        assertEquals(annPath, workspace.getAnnotationFile());
        assertEquals(immPath, workspace.getResistanceFile());
    }

    @Test(expected = FileNotFoundException.class)
    public void testNotFound() throws Exception {
        Workspace workspace = new Workspace(null);

        workspace.load();
    }

    @Test(expected = FileNotFoundException.class)
    public void testLoadNoAnnotationFile() throws Exception {
        Workspace workspace1 = new Workspace(null);
        File[] pathList = new File[5];

        File edgePath1 = new File("mygraph.edge.graph");
        File nodePath1 = new File("mygraph.node.graph");
        File nwkPath1 = new File("mygraph.nwk");
        File annPath1 = new File("not");
        File immPath1 = new File("mygraph.imm.csv");

        pathList[0] = edgePath1;
        pathList[1] = nodePath1;
        pathList[2] = nwkPath1;
        pathList[3] = annPath1;
        pathList[4] = immPath1;

        workspace1.files = pathList;

        workspace1.load();
    }

    @Test(expected = FileNotFoundException.class)
    public void testLoadNoNwkFile() throws Exception {
        Workspace workspace1 = new Workspace(null);
        File[] pathList = new File[5];

        File edgePath1 = new File("mygraph.edge.graph");
        File nodePath1 = new File("mygraph.node.graph");
        File nwkPath1 = new File("not");
        File annPath1 = new File("mygraph.ann.csv");
        File immPath1 = new File("mygraph.imm.csv");

        pathList[0] = edgePath1;
        pathList[1] = nodePath1;
        pathList[2] = nwkPath1;
        pathList[3] = annPath1;
        pathList[4] = immPath1;

        workspace1.files = pathList;

        workspace1.load();

    }

    @Test(expected = FileNotFoundException.class)
    public void testLoadNoNodeFile() throws Exception {
        Workspace workspace1 = new Workspace(null);
        File[] pathList = new File[5];

        File edgePath1 = new File("mygraph.edge.graph");
        File nodePath1 = new File("not");
        File nwkPath1 = new File("mygraph.nwk");
        File annPath1 = new File("mygraph.ann.csv");
        File immPath1 = new File("mygraph.imm.csv");

        pathList[0] = edgePath1;
        pathList[1] = nodePath1;
        pathList[2] = nwkPath1;
        pathList[3] = annPath1;
        pathList[4] = immPath1;

        workspace1.files = pathList;
        workspace1.load();

    }

    @Test(expected = FileNotFoundException.class)
    public void testLoadNoEdgeFile() throws Exception {
        Workspace workspace1 = new Workspace(null);
        File[] pathList = new File[4];

        File edgePath1 = new File("not");
        File nodePath1 = new File("mygraph.node.graph");
        File nwkPath1 = new File("mygraph.nwk");
        File annPath1 = new File("mygraph.ann.csv");
        File immPath1 = new File("mygraph.imm.csv");

        pathList[0] = edgePath1;
        pathList[1] = nodePath1;
        pathList[2] = nwkPath1;
        pathList[3] = annPath1;

        workspace1.files = pathList;

        workspace1.load();

    }

}
