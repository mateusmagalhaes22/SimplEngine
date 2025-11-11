package com.example.simplengine.GameObjects;

import com.example.simplengine.Vectors.Vector2;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class SpriteObject extends GameObject {

    private BufferedImage image;
    private int width;
    private int height;
    private float scaleX;
    private float scaleY;

    /**
     * Cria um Sprite carregando uma imagem do caminho especificado.
     * A imagem mantém suas dimensões originais.
     * 
     * @param imagePath Caminho para o arquivo de imagem (suporta PNG, JPG, BMP, GIF)
     * @param posX Posição X inicial
     * @param posY Posição Y inicial
     * @throws IOException Se o arquivo não for encontrado ou não puder ser lido
     */
    public SpriteObject(String imagePath, int posX, int posY) throws IOException {
        super();
        loadImage(imagePath);
        setPosition(new Vector2(posX, posY));
        this.scaleX = 1.0f;
        this.scaleY = 1.0f;
    }

    /**
     * Cria um Sprite carregando uma imagem do caminho especificado.
     * A imagem é redimensionada para as dimensões especificadas.
     * 
     * @param imagePath Caminho para o arquivo de imagem
     * @param posX Posição X inicial
     * @param posY Posição Y inicial
     * @param width Largura desejada da imagem
     * @param height Altura desejada da imagem
     * @throws IOException Se o arquivo não for encontrado ou não puder ser lido
     */
    public SpriteObject(String imagePath, int posX, int posY, int width, int height) throws IOException {
        super();
        loadImage(imagePath);
        setPosition(new Vector2(posX, posY));
        this.width = width;
        this.height = height;
        this.scaleX = 1.0f;
        this.scaleY = 1.0f;
    }

    /**
     * Cria um Sprite sem passar uma imagem.
     * A imagem deve ser definida posteriormente.
     * @param posX Posição X inicial
     * @param posY Posição Y inicial
     * @param width Largura desejada da imagem
     * @param height Altura desejada da imagem
     * @throws IOException Se o arquivo não for encontrado ou não puder ser lido
     */
    public SpriteObject(int posX, int posY) throws IOException {
        super();
        setPosition(new Vector2(posX, posY));
        this.scaleX = 1.0f;
        this.scaleY = 1.0f;
    }

    /**
     * Cria um Sprite a partir de um BufferedImage existente.
     * 
     * @param image Imagem a ser renderizada
     * @param posX Posição X inicial
     * @param posY Posição Y inicial
     */
    public SpriteObject(BufferedImage image, int posX, int posY) {
        super();
        this.image = image;
        this.width = image.getWidth();
        this.height = image.getHeight();
        setPosition(new Vector2(posX, posY));
        this.scaleX = 1.0f;
        this.scaleY = 1.0f;
    }

    private void loadImage(String imagePath) throws IOException {
        File imageFile = new File(imagePath);
        this.image = ImageIO.read(imageFile);
        
        if (this.image == null) {
            throw new IOException("Não foi possível carregar a imagem: " + imagePath);
        }
        
        this.width = image.getWidth();
        this.height = image.getHeight();
    }

    public void setScale(float scaleX, float scaleY) {
        this.scaleX = scaleX;
        this.scaleY = scaleY;
    }

    public void setScale(float scale) {
        this.scaleX = scale;
        this.scaleY = scale;
    }

    public void setSize(int width, int height) {
        this.width = width;
        this.height = height;
    }

    /**
     * Retorna a largura base do sprite (sem aplicar a escala).
     * 
     * @return Largura base em pixels
     */
    public int getBaseWidth() {
        return width;
    }

    /**
     * Retorna a altura base do sprite (sem aplicar a escala).
     * 
     * @return Altura base em pixels
     */
    public int getBaseHeight() {
        return height;
    }

    /**
     * Retorna a largura final do sprite (com escala aplicada).
     * 
     * @return Largura final em pixels
     */
    public int getWidth() {
        return (int) (width * scaleX);
    }

    /**
     * Retorna a altura final do sprite (com escala aplicada).
     * 
     * @return Altura final em pixels
     */
    public int getHeight() {
        return (int) (height * scaleY);
    }

    public BufferedImage getImage() {
        return image;
    }

    /**
     * Define a imagem do sprite e atualiza as dimensões para o tamanho original da imagem.
     * 
     * @param image Nova imagem a ser renderizada
     */
    public void setImage(BufferedImage image) {
        this.image = image;
        if (image != null) {
            this.width = image.getWidth();
            this.height = image.getHeight();
        }
    }

    public void setImage(String imagePath) throws IOException {
        BufferedImage img = ImageIO.read(new File(imagePath));
        setImage(img);
    }

    /**
     * Define a imagem do sprite sem alterar as dimensões atuais.
     * Útil quando você quer trocar a imagem mas manter o tamanho definido.
     * 
     * @param image Nova imagem a ser renderizada
     * @param keepSize Se true, mantém width e height atuais; se false, atualiza para o tamanho da imagem
     */
    public void setImage(BufferedImage image, boolean keepSize) {
        this.image = image;
        if (!keepSize && image != null) {
            this.width = image.getWidth();
            this.height = image.getHeight();
        }
    }

    /**
     * Define a imagem do sprite a partir de uma região específica de uma imagem maior.
     * Útil para trabalhar com spritesheets.
     * 
     * @param sourceImage A imagem original de onde extrair a subimagem
     * @param x Posição X inicial da região a ser extraída
     * @param y Posição Y inicial da região a ser extraída
     * @param width Largura da região a ser extraída
     * @param height Altura da região a ser extraída
     * @throws IllegalArgumentException Se as coordenadas ou dimensões forem inválidas
     */
    public void setImageFromSubImage(BufferedImage sourceImage, int x, int y, int width, int height) {
        setImageFromSubImage(sourceImage, x, y, width, height, false);
    }

    /**
     * Define a imagem do sprite a partir de uma região específica de uma imagem maior.
     * Útil para trabalhar com spritesheets.
     * 
     * @param sourceImage A imagem original de onde extrair a subimagem
     * @param x Posição X inicial da região a ser extraída
     * @param y Posição Y inicial da região a ser extraída
     * @param width Largura da região a ser extraída
     * @param height Altura da região a ser extraída
     * @param keepSize Se true, mantém width e height atuais do sprite; se false, usa o tamanho da região extraída
     * @throws IllegalArgumentException Se as coordenadas ou dimensões forem inválidas
     */
    public void setImageFromSubImage(BufferedImage sourceImage, int x, int y, int width, int height, boolean keepSize) {
        if (sourceImage == null) {
            throw new IllegalArgumentException("A imagem de origem não pode ser nula");
        }
        
        if (x < 0 || y < 0 || width <= 0 || height <= 0) {
            throw new IllegalArgumentException("As coordenadas e dimensões devem ser positivas");
        }
        
        if (x + width > sourceImage.getWidth() || y + height > sourceImage.getHeight()) {
            throw new IllegalArgumentException(
                String.format("A região especificada (%d,%d,%d,%d) está fora dos limites da imagem (%d,%d)",
                    x, y, width, height, sourceImage.getWidth(), sourceImage.getHeight())
            );
        }
        
        this.image = sourceImage.getSubimage(x, y, width, height);
        if (!keepSize) {
            this.width = width;
            this.height = height;
        }
    }

    /**
     * Define a imagem do sprite a partir de uma região específica de uma imagem carregada de um arquivo.
     * Útil para trabalhar com spritesheets.
     * 
     * @param imagePath Caminho para o arquivo de imagem
     * @param x Posição X inicial da região a ser extraída
     * @param y Posição Y inicial da região a ser extraída
     * @param width Largura da região a ser extraída
     * @param height Altura da região a ser extraída
     * @throws IOException Se o arquivo não for encontrado ou não puder ser lido
     * @throws IllegalArgumentException Se as coordenadas ou dimensões forem inválidas
     */
    public void setImageFromSubImage(String imagePath, int x, int y, int width, int height) throws IOException {
        setImageFromSubImage(imagePath, x, y, width, height, false);
    }

    /**
     * Define a imagem do sprite a partir de uma região específica de uma imagem carregada de um arquivo.
     * Útil para trabalhar com spritesheets.
     * 
     * @param imagePath Caminho para o arquivo de imagem
     * @param x Posição X inicial da região a ser extraída
     * @param y Posição Y inicial da região a ser extraída
     * @param width Largura da região a ser extraída
     * @param height Altura da região a ser extraída
     * @param keepSize Se true, mantém width e height atuais do sprite; se false, usa o tamanho da região extraída
     * @throws IOException Se o arquivo não for encontrado ou não puder ser lido
     * @throws IllegalArgumentException Se as coordenadas ou dimensões forem inválidas
     */
    public void setImageFromSubImage(String imagePath, int x, int y, int width, int height, boolean keepSize) throws IOException {
        File imageFile = new File(imagePath);
        BufferedImage sourceImage = ImageIO.read(imageFile);
        
        if (sourceImage == null) {
            throw new IOException("Não foi possível carregar a imagem: " + imagePath);
        }
        
        setImageFromSubImage(sourceImage, x, y, width, height, keepSize);
    }

    @Override
    public void render(Graphics2D g) {
        if (g == null || image == null) return;

        Vector2 pos = getPosition();
        int x = (int) pos.getX();
        int y = (int) pos.getY();
        int w = getWidth();
        int h = getHeight();

        g.drawImage(image, x, y, w, h, null);
    }

    @Override
    public Rectangle2D.Float getBounds() {
        Vector2 pos = getPosition();
        return new Rectangle2D.Float(
            (float) pos.getX(),
            (float) pos.getY(),
            getWidth(),
            getHeight()
        );
    }

    @Override
    public boolean intersects(GameObject other) {
        Rectangle2D.Float a = this.getBounds();
        Rectangle2D.Float b = other.getBounds();
        return a.x <= b.x + b.width &&
               a.x + a.width >= b.x &&
               a.y <= b.y + b.height &&
               a.y + a.height >= b.y;
    }
}
