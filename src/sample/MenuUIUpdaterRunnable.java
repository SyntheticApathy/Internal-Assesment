package sample;

import javafx.scene.text.Text;

import static sample.logicalgameplay.GameLogic.getRoundNumber;

public class MenuUIUpdaterRunnable implements Runnable {
    private boolean shouldRun = true;
    private final Text enemiesKilledText;
    private final Text turretsToBePlaced;
    private final Text roundNumber;

    public MenuUIUpdaterRunnable(Text enemiesKilledText, Text turretsToBePlaced, Text roundNumber) {
        this.enemiesKilledText = enemiesKilledText;
        this.turretsToBePlaced = turretsToBePlaced;
        this.roundNumber = roundNumber;
    }

    public void setShouldRun(boolean shouldRun) {
        this.shouldRun = shouldRun;
    }

    @Override
    public void run() {
        while (shouldRun) {
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                //will not happen
            }
            System.out.print(".");
            enemiesKilledText.setText("Enemies killed so far: " + GameUI.enemiesKilled);
            turretsToBePlaced.setText(GameUI.amountOfTurretsWhichCanBePlaced[0] + " turrets can be placed");
            roundNumber.setText("Round: " + getRoundNumber());
        }
    }
}

