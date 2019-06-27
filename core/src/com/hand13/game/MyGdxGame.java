package com.hand13.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

public class MyGdxGame extends ApplicationAdapter {
    SpriteBatch batch;
    Texture img;
    Texture foodImg;
    Rectangle imgPosition;
    private Direction currentDirection = Direction.RIGHT;
    private Node header;
    private Node tail;
    private boolean update = false;
    private final int snakeSize = 25;
    private final int worldSize = 800;
    private Node food;

    public MyGdxGame() {
        header = new Node();
        food = new Node();
        randomFoodPosition();
        header.x = 2;
        header.y = 2;
    }

    private void randomFoodPosition() {
        food.x = (int) (Math.random() * 20);
        food.y = (int) (Math.random() * 20);
    }

    @Override
    public void create() {
        batch = new SpriteBatch();
        img = new Texture("snake.png");
        foodImg = new Texture("snake.png");
        imgPosition = new Rectangle();
        imgPosition.x = 0;
        imgPosition.y = 0;
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(1, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        processInput();
        drawSnake();
        drawFood();
    }

    private void processInput() {
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            currentDirection = Direction.LEFT;
            update = true;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            currentDirection = Direction.RIGHT;
            update = true;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            currentDirection = Direction.DOWN;
            update = true;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            currentDirection = Direction.UP;
            update = true;
        }
        if (update) {
            next();
            update = false;
        }
    }

    private void processSnake() {
        Node tmp = new Node();
        switch (currentDirection) {
            case UP:
                tmp.x = header.x;
                tmp.y = header.y - 1;
                break;
            case DOWN:
                tmp.x = header.x;
                tmp.y = header.y + 1;
                break;
            case LEFT:
                tmp.x = header.x - 1;
                tmp.y = header.y;
                break;
            case RIGHT:
                tmp.x = header.x + 1;
                tmp.y = header.y;
                break;
            default:
        }
        tmp.next = header;
        header.prev = tmp;
        header = tmp;
        if (header.x == food.x && header.y == food.y) {
            randomFoodPosition();
            if (tail == null) {
                tail = header.next;
            }
        } else {
            if (tail != null) {
                tail = tail.prev;
                tail.next = null;
            } else {
                header.next = null;
            }
        }
    }

    private void drawSnake() {
        batch.begin();
        Node currentNode = header;
        while (currentNode != null) {
            batch.draw(img, currentNode.x * snakeSize, currentNode.y * snakeSize, snakeSize, snakeSize);
            currentNode = currentNode.next;
        }
        batch.end();

    }

    private void drawFood(){
        batch.begin();
        batch.draw(foodImg,food.x * snakeSize,food.y * snakeSize,snakeSize,snakeSize);
        batch.end();
    }

    private void next() {
        processSnake();
    }

    @Override
    public void dispose() {
        batch.dispose();
        img.dispose();
    }
}

class Node {
    Node prev;
    int x;
    int y;
    Node next;
}
