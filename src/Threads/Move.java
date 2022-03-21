package Threads;

import CellClasses.Cell;
import CellClasses.CellObstacle;
import MVC.Model;

import java.util.TimerTask;

public class Move extends TimerTask {
    CharacterClasses.Character movingChar;
    Cell initialPos, finalPos;
    double coefDirX;
    double coefDirY;
    Cell currentTarget;
    public boolean isMoving = false;
    Model model;
    public Move(CharacterClasses.Character c, Cell start){
        super();
        this.initialPos = start;
        this.finalPos = start;
        this.movingChar = c;
        this.model = c.model;
    }

    public void setDestination(Cell end) {
        this.finalPos = end;

        isMoving = true;
        currentTarget = initialPos;
        double max = Double.MAX_VALUE;
        for (Cell ngh : model.grid.getNeighbors(initialPos)) {
            double comp = Math.sqrt(Math.pow(end.posCenterX - ngh.posCenterX, 2) + Math.pow(end.posCenterY - ngh.posCenterY, 2));
            if ( comp < max && (!ngh.isTargeted
                    || (ngh.getCellCharacterContent() != null
                    && ngh.getCellCharacterContent().type == "enemy"
                    && initialPos.getCellCharacterContent().type != "enemy"))
                    && (ngh.getCellContent() == null
                    || ngh.getCellContent().getClass() != CellObstacle.class
                    || movingChar.isFlying)
                    && (ngh.getCellCharacterContent() == null
                    || (initialPos.getCellCharacterContent() != null
                    && ngh.getCellCharacterContent().type == "enemy"
                    && initialPos.getCellCharacterContent().type != "enemy"))) {
                max = comp;
                currentTarget = ngh;
            }
        }
        currentTarget.setTargeted(movingChar);
        this.coefDirX = (currentTarget.posCenterX - initialPos.posCenterX)/100.;
        this.coefDirY = (currentTarget.posCenterY - initialPos.posCenterY)/100.;
    }
    @Override
    public void run()  {
        if (isMoving && (coefDirX > 0 && currentTarget.posCenterX > movingChar.contentPosX + Model.cellSize/2.)
                || (coefDirX < 0 && currentTarget.posCenterX < movingChar.contentPosX + Model.cellSize/2.)
                || (coefDirY > 0 && currentTarget.posCenterY > movingChar.contentPosY + Model.cellSize/2.)
                || (coefDirY < 0 && currentTarget.posCenterY < movingChar.contentPosY + Model.cellSize/2.)){
            if ((coefDirX > 0 && currentTarget.posCenterX > movingChar.contentPosX + Model.cellSize/2.) || (coefDirX < 0 && currentTarget.posCenterX < movingChar.contentPosX + Model.cellSize/2.))
                movingChar.contentPosX += movingChar.speed * coefDirX;
            if ((coefDirY > 0 && currentTarget.posCenterY > movingChar.contentPosY + Model.cellSize/2.) || (coefDirY < 0 && currentTarget.posCenterY < movingChar.contentPosY + Model.cellSize/2.))
                movingChar.contentPosY += movingChar.speed * coefDirY;
        }
        else if (isMoving) {

            movingChar.setContentCellPosition(currentTarget);
            initialPos.setCellCharacterContent(null);
            currentTarget.setCellCharacterContent(movingChar);
            currentTarget.isTargeted = false;
            Cell previous = initialPos;
            initialPos = currentTarget;
            double max = Double.MAX_VALUE;
            for (Cell ngh : model.grid.getNeighbors(initialPos)) {
                if (ngh != previous) {
                    double comp = Math.sqrt(Math.pow(finalPos.posCenterX - ngh.posCenterX, 2) + Math.pow(finalPos.posCenterY - ngh.posCenterY, 2));

                    if ( comp < max && (!ngh.isTargeted
                                            || (ngh.getCellCharacterContent() != null
                                                  && ngh.getCellCharacterContent().type == "enemy"
                                                  && initialPos.getCellCharacterContent().type != "enemy"))
                                    && (ngh.getCellContent() == null
                                            || ngh.getCellContent().getClass() != CellObstacle.class
                                            || movingChar.isFlying)
                                    && (ngh.getCellCharacterContent() == null
                                            || (initialPos.getCellCharacterContent() != null
                                                && ngh.getCellCharacterContent().type == "enemy"
                                                && initialPos.getCellCharacterContent().type != "enemy"))) {
                        max = comp;
                        currentTarget = ngh;
                    }

                }
            }
            currentTarget.setTargeted(movingChar);
            if (initialPos == finalPos) {
                isMoving = false;
                movingChar.timer.cancel();
            } else {
                this.coefDirX = (currentTarget.posCenterX - initialPos.posCenterX)/100.;
                this.coefDirY = (currentTarget.posCenterY - initialPos.posCenterY)/100.;
            }
        }
    }
}

