package com.gr15.pacman.model.entities;

import java.util.ArrayList;
import java.util.List;
import com.gr15.pacman.model.Board;
import com.gr15.pacman.model.Board.TileType;
import com.gr15.pacman.model.Position;

/**
 * Ghost
 */
public class Ghost
    extends Entity{
    private Pacman pacman;
    private GhostType ghostType;

    public enum GhostType { RED, BLUE, PINK, ORANGE };
        
    public Ghost(Position startPos, double speed, double radius,
        Pacman pacman, GhostType type) { super(startPos, radius, speed);
        this.pacman = pacman;
        this.ghostType = type;
    }
    @Override
    public void move(Board board, double deltaSeconds) {
        switch (ghostType) {
            case RED -> setDirection(chasePacman(board, pacman));
            case BLUE -> setDirection(predictPacman(board, pacman));
            case PINK -> setDirection(chasePacman(board, pacman));
            case ORANGE -> setDirection(randomize(board));
            default -> setDirection(chasePacman(board, pacman));
        }
        super.move(board, deltaSeconds);
    }

    public boolean canMove(Board board, Position pos, Direction dir) {
        if (dir == null || dir == Direction.NONE) { return false; }

        Position next = pos.offset(dir);
        if (!next.inBounds(board.getTileBoard())) { return false; }

        return board.getTile(next) != TileType.WALL;
    }
    /**
     * Method that chooses direction based on where pacman currently is.
     * @param board the current board
     * @param pac entity pacman
     * @return return an available direction that is in the direction of pacman.
     */
    public Direction chasePacman(Board board, Pacman pac){
        //This is a dumb ghost just gets pacmans position and trys to go there even if there is a wall.
        //Needs refining so that ghost can loop around objects if needed.
        Position ghostPos = this.getPosition();
        Position PacPos = pac.getPosition();

        int pacX = PacPos.x();
        int pacY = PacPos.y();
        int ghostX = ghostPos.x();
        int ghostY = ghostPos.y();
        Direction plannedDir = Direction.NONE;
        //isAtIntersection() is so that the ghost only move when at a intersection so they don't 
        // just move back and forth
        if(!isAtIntersection(board, ghostPos) && canMove(board,ghostPos,getDirection())){                           
            return getDirection();
        }
        else{
            int dx = pacX-ghostX;
            int dy = pacY-ghostY;
            if(Math.abs(dx) > Math.abs(dy)){ //prioritize they direction that is the furthest from pacman
                if (dx < 0 && canMove(board, ghostPos, Direction.LEFT)) return Direction.LEFT;
                if (dx > 0 && canMove(board, ghostPos, Direction.RIGHT)) return Direction.RIGHT;
                if (dy < 0 && canMove(board, ghostPos, Direction.UP)) return Direction.UP;
                if (dy > 0 && canMove(board, ghostPos, Direction.DOWN)) return Direction.DOWN;
            } else {
                if (dy < 0 && canMove(board, ghostPos, Direction.UP)) return Direction.UP;
                if (dy > 0 && canMove(board, ghostPos, Direction.DOWN)) return Direction.DOWN;
                if (dx < 0 && canMove(board, ghostPos, Direction.LEFT)) return Direction.LEFT;
                if (dx > 0 && canMove(board, ghostPos, Direction.RIGHT)) return Direction.RIGHT;
            }
            
        }
        return plannedDir;
    }
    /**
     * Method that chooses direction based on a predict path of pacman
     * @param board current board
     * @param pacman entity pacman
     * @return an available direction that is towards pacmans predicted path.
     */
    public Direction predictPacman(Board board, Pacman pacman){
        Position ghostPos = this.getPosition();
        Position predictedPos = predictedPacPos(board, pacman);
    
        int pacX = predictedPos.x();
        int pacY = predictedPos.y();
        int ghostX = ghostPos.x();
        int ghostY = ghostPos.y();

        int dx = pacX-ghostX;
        int dy = pacY-ghostY;

        if(!canMove(board,ghostPos,getDirection()) || getDirection() == Direction.NONE){
            if(dx > dy){ //prioritize they direction that is the furthest from pacman
                if(dx < 0 && canMove(board,ghostPos,Direction.LEFT)){ return Direction.LEFT; }
                else if(dx > 0 && canMove(board,ghostPos,Direction.RIGHT)){ return Direction.RIGHT; }
                else if(dy < 0 && canMove(board,ghostPos,Direction.UP)){ return Direction.UP; }
                else if(dy > 0 && canMove(board,ghostPos,Direction.DOWN)){ return Direction.DOWN; }
            }
            else {
                if (dy < 0 && canMove(board, ghostPos, Direction.UP)) return Direction.UP;
                else if (dy > 0 && canMove(board, ghostPos, Direction.DOWN)) return Direction.DOWN;
                else if (dx < 0 && canMove(board, ghostPos, Direction.LEFT)) return Direction.LEFT;
                else if (dx > 0 && canMove(board, ghostPos, Direction.RIGHT)) return Direction.RIGHT;
            
            }
            //missing fallback when it gets stuck
        }
        return getDirection();
    }
    /**
     * Method that chooses a random direction based on if it is at a intersection
     * @param board current board
     * @return random available direction
     */
    public Direction randomize(Board board){
        // this function is for the last ghost which goes just random
        // maybe later make the ghost go nearer pacman for a bit if it is too far away then back to ramdom
        Position pos = this.getPosition();
        Position ghostPos = this.getPosition();
        Direction dir = this.getDirection();
        int count = 0;
        if (!isAtIntersection(board, pos) && canMove(board,ghostPos,getDirection())) {
            return dir;
        }
        List<Direction> options = new ArrayList<>();
        for (Direction d : Direction.values()) {
            if (d == Direction.NONE || d == getOpposite(dir)) { continue; };
                if (canMove(board, pos, d)) {
                    options.add(d);
                }
            }

        int randomIndex = (int)(Math.random() * count); // random int from 0 to count-1
        return options.get(randomIndex);        
    }
    /**
     * Helper function that predicts pacmans position a couple of tiles in advance
     * @param board current board
     * @param pac entity pacman
     * @return a predicted position of pacman a couple of tiles ahead
     */
    public Position predictedPacPos(Board board, Pacman pac){
        Direction dir = pac.getDirection();
        Position predicted = pac.getPosition(); // next tile for pacman
        for(int i = 0; i < 3; i++){
            Position next = predicted.offset(dir);
            if(next.inBounds(board.getTileBoard())){ //checks if next tile is in bounds
                predicted = predicted.offset(dir);
            }
        }
        return predicted; //returns 3 tiles ahead of pacman if 3 tiles ahead is in bounds otherwise the end of the row/column of where pacman is going.
    }
    /**
     * This method is for when pacman eats a power-pallet and 
     * the ghosts get scared, the logic is mirrored with the chasePacman() function
     * ie. move away from pacman when scared instead of towards him
     * @param board current board
     * @param pac entity pacman
     * @return a Direction that is away from pacman, to flee.
     */
    public Direction scaredGhost(Board board, Pacman pac){ // is currently not used need scared logic and rendering.
        Position ghostPos = this.getPosition();
        Position PacPos = pac.getPosition();

        int pacX = PacPos.x();
        int pacY = PacPos.y();
        int ghostX = ghostPos.x();
        int ghostY = ghostPos.y();
        Direction plannedDir = Direction.NONE;
        if(!isAtIntersection(board, ghostPos) && canMove(board,ghostPos, getDirection())){                           
            plannedDir = getDirection();
        }
        else{
            int dx = pacX-ghostX;
            int dy = pacY-ghostY;
            if(dx > dy){ //prioritize they direction that is the furthest from pacman
                if(pacX>ghostX && canMove(board,ghostPos,Direction.LEFT)){ return Direction.LEFT; }
                else if(pacX<ghostX && canMove(board,ghostPos,Direction.RIGHT)){ return Direction.RIGHT; }
                else if(pacY>ghostY && canMove(board,ghostPos,Direction.UP)){ return Direction.UP; }
                else if(pacY<ghostY && canMove(board,ghostPos,Direction.DOWN)){ return Direction.DOWN; }
            }
            else {
                if (pacY > ghostY && canMove(board, ghostPos, Direction.UP)) return Direction.UP;
                else if (pacY < ghostY && canMove(board, ghostPos, Direction.DOWN)) return Direction.DOWN;
                else if (pacX > ghostX && canMove(board, ghostPos, Direction.LEFT)) return Direction.LEFT;
                else if (pacX < ghostX && canMove(board, ghostPos, Direction.RIGHT)) return Direction.RIGHT;
            }
            
        }
        return plannedDir;
    }
    /**
     * Helper function that returns if the entity is at an intersection or not
     * @param board current board
     * @param pos postition of the entity
     * @return a boolean, that is true if there are more than 2 ways an entity can move to
     */
    private boolean isAtIntersection(Board board, Position pos) {
        int movableCount = 0;
        for (Direction dir : Direction.values()) {
            if (dir != Direction.NONE && board.isMovable(pos.offset(dir))) {
                movableCount++;
            }
        }
        return movableCount > 2;
    }
    /**
     * Helper function to get the opposite direction of the given argument
     * @param dir direction of the entity
     * @return the opposite direction
     */
    private Direction getOpposite(Direction dir) {
        return switch (dir) {
            case UP -> Direction.DOWN;
            case DOWN -> Direction.UP;
            case LEFT -> Direction.RIGHT;
            case RIGHT -> Direction.LEFT;
            default -> Direction.NONE;
        };
    }

    public GhostType getGhostType() {
        return this.ghostType;
    }
}
