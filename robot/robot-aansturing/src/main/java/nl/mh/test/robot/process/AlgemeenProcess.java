package nl.mh.test.robot.process;

/**
 * Created by Marc on 16-11-2015.
 */
public class AlgemeenProcess {

    public void startProcess(){
        start();
        if(moetNieuwPipet()) {
            nieuwPipet();
        }
        waterhalen();
        plaatsdruppel();
        sluitAf();
    }


    private void start() {

    }

    private boolean moetNieuwPipet() {
        return false;
    }

    private void nieuwPipet() {

    }

    private void waterhalen() {

    }

    private void plaatsdruppel() {
    }

    private void sluitAf() {

    }


}
