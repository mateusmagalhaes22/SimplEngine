package com.example.simplengine.GameObjects;

import static org.junit.jupiter.api.Assertions.*;

import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import com.example.simplengine.Vectors.Vector2;

public class SpriteTest {

    private static final double DELTA = 0.001;
    
    @TempDir
    static File tempDir;
    
    private static File testImageFile;

    @BeforeAll
    static void setupTestImage() throws IOException {
        // Cria uma imagem de teste de 100x50 pixels
        BufferedImage testImage = new BufferedImage(100, 50, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = testImage.createGraphics();
        g.fillRect(0, 0, 100, 50);
        g.dispose();
        
        testImageFile = new File(tempDir, "test_image.png");
        ImageIO.write(testImage, "png", testImageFile);
    }

    @Test
    void sprite_constructorWithPath_loadsImage() throws IOException {
        SpriteObject sprite = new SpriteObject(testImageFile.getAbsolutePath(), 10, 20);
        
        assertNotNull(sprite.getImage(), "Imagem deve ser carregada");
        assertEquals(100, sprite.getWidth(), "Largura deve ser 100");
        assertEquals(50, sprite.getHeight(), "Altura deve ser 50");
        assertEquals(10.0, sprite.getPosition().getX(), DELTA, "Posição X deve ser 10");
        assertEquals(20.0, sprite.getPosition().getY(), DELTA, "Posição Y deve ser 20");
    }

    @Test
    void sprite_constructorWithSize_resizesImage() throws IOException {
        SpriteObject sprite = new SpriteObject(testImageFile.getAbsolutePath(), 10, 20, 200, 100);
        
        assertNotNull(sprite.getImage(), "Imagem deve ser carregada");
        assertEquals(200, sprite.getWidth(), "Largura deve ser 200");
        assertEquals(100, sprite.getHeight(), "Altura deve ser 100");
    }

    @Test
    void sprite_constructorWithBufferedImage_usesImage() {
        BufferedImage image = new BufferedImage(80, 60, BufferedImage.TYPE_INT_ARGB);
        SpriteObject sprite = new SpriteObject(image, 5, 15);
        
        assertSame(image, sprite.getImage(), "Deve usar a imagem fornecida");
        assertEquals(80, sprite.getWidth(), "Largura deve ser 80");
        assertEquals(60, sprite.getHeight(), "Altura deve ser 60");
        assertEquals(5.0, sprite.getPosition().getX(), DELTA, "Posição X deve ser 5");
        assertEquals(15.0, sprite.getPosition().getY(), DELTA, "Posição Y deve ser 15");
    }

    @Test
    void sprite_constructorWithInvalidPath_throwsIOException() {
        assertThrows(IOException.class, () -> {
            new SpriteObject("/caminho/invalido/imagem.png", 0, 0);
        }, "Deve lançar IOException para caminho inválido");
    }

    @Test
    void sprite_setScale_uniform_updatesSize() throws IOException {
        SpriteObject sprite = new SpriteObject(testImageFile.getAbsolutePath(), 0, 0);
        
        sprite.setScale(2.0f); // 2x maior
        
        assertEquals(200, sprite.getWidth(), "Largura deve ser 200 (100 * 2)");
        assertEquals(100, sprite.getHeight(), "Altura deve ser 100 (50 * 2)");
    }

    @Test
    void sprite_setScale_nonUniform_updatesSize() throws IOException {
        SpriteObject sprite = new SpriteObject(testImageFile.getAbsolutePath(), 0, 0);
        
        sprite.setScale(2.0f, 0.5f); // 2x largura, 0.5x altura
        
        assertEquals(200, sprite.getWidth(), "Largura deve ser 200 (100 * 2)");
        assertEquals(25, sprite.getHeight(), "Altura deve ser 25 (50 * 0.5)");
    }

    @Test
    void sprite_setSize_changesSize() throws IOException {
        SpriteObject sprite = new SpriteObject(testImageFile.getAbsolutePath(), 0, 0);
        
        sprite.setSize(150, 75);
        
        assertEquals(150, sprite.getWidth(), "Largura deve ser 150");
        assertEquals(75, sprite.getHeight(), "Altura deve ser 75");
    }

    @Test
    void sprite_setImage_updatesImage() throws IOException {
        SpriteObject sprite = new SpriteObject(testImageFile.getAbsolutePath(), 0, 0);
        
        BufferedImage newImage = new BufferedImage(120, 80, BufferedImage.TYPE_INT_ARGB);
        sprite.setImage(newImage);
        
        assertSame(newImage, sprite.getImage(), "Deve atualizar a imagem");
        assertEquals(120, sprite.getWidth(), "Largura deve ser atualizada para 120");
        assertEquals(80, sprite.getHeight(), "Altura deve ser atualizada para 80");
    }

    @Test
    void sprite_setImage_withKeepSize_preservesDimensions() throws IOException {
        SpriteObject sprite = new SpriteObject(testImageFile.getAbsolutePath(), 0, 0);
        sprite.setSize(50, 50); // Define tamanho customizado
        
        BufferedImage newImage = new BufferedImage(120, 80, BufferedImage.TYPE_INT_ARGB);
        sprite.setImage(newImage, true); // Mantém o tamanho
        
        assertSame(newImage, sprite.getImage(), "Deve atualizar a imagem");
        assertEquals(50, sprite.getWidth(), "Largura deve permanecer 50");
        assertEquals(50, sprite.getHeight(), "Altura deve permanecer 50");
    }

    @Test
    void sprite_setImage_withoutKeepSize_updatesDimensions() throws IOException {
        SpriteObject sprite = new SpriteObject(testImageFile.getAbsolutePath(), 0, 0);
        sprite.setSize(50, 50); // Define tamanho customizado
        
        BufferedImage newImage = new BufferedImage(120, 80, BufferedImage.TYPE_INT_ARGB);
        sprite.setImage(newImage, false); // Atualiza o tamanho
        
        assertSame(newImage, sprite.getImage(), "Deve atualizar a imagem");
        assertEquals(120, sprite.getWidth(), "Largura deve ser atualizada para 120");
        assertEquals(80, sprite.getHeight(), "Altura deve ser atualizada para 80");
    }

    @Test
    void sprite_getBaseWidthAndHeight_returnsBaseValues() throws IOException {
        SpriteObject sprite = new SpriteObject(testImageFile.getAbsolutePath(), 0, 0);
        sprite.setScale(2.0f);
        
        assertEquals(100, sprite.getBaseWidth(), "Largura base deve ser 100");
        assertEquals(50, sprite.getBaseHeight(), "Altura base deve ser 50");
        assertEquals(200, sprite.getWidth(), "Largura com escala deve ser 200");
        assertEquals(100, sprite.getHeight(), "Altura com escala deve ser 100");
    }

    @Test
    void sprite_getBounds_returnsCorrectBounds() throws IOException {
        SpriteObject sprite = new SpriteObject(testImageFile.getAbsolutePath(), 10, 20);
        
        Rectangle2D.Float bounds = sprite.getBounds();
        
        assertEquals(10.0f, bounds.x, DELTA, "X do bounds deve ser 10");
        assertEquals(20.0f, bounds.y, DELTA, "Y do bounds deve ser 20");
        assertEquals(100.0f, bounds.width, DELTA, "Largura do bounds deve ser 100");
        assertEquals(50.0f, bounds.height, DELTA, "Altura do bounds deve ser 50");
    }

    @Test
    void sprite_getBounds_withScale_returnsScaledBounds() throws IOException {
        SpriteObject sprite = new SpriteObject(testImageFile.getAbsolutePath(), 10, 20);
        sprite.setScale(2.0f);
        
        Rectangle2D.Float bounds = sprite.getBounds();
        
        assertEquals(10.0f, bounds.x, DELTA, "X do bounds deve ser 10");
        assertEquals(20.0f, bounds.y, DELTA, "Y do bounds deve ser 20");
        assertEquals(200.0f, bounds.width, DELTA, "Largura do bounds deve ser 200");
        assertEquals(100.0f, bounds.height, DELTA, "Altura do bounds deve ser 100");
    }

    @Test
    void sprite_intersects_detectsCollision() throws IOException {
        SpriteObject sprite1 = new SpriteObject(testImageFile.getAbsolutePath(), 0, 0);   // 100x50 em (0,0)
        SpriteObject sprite2 = new SpriteObject(testImageFile.getAbsolutePath(), 50, 25); // 100x50 em (50,25)
        
        assertTrue(sprite1.intersects(sprite2), "Sprites devem se intersectar");
        assertTrue(sprite2.intersects(sprite1), "Interseção deve ser simétrica");
    }

    @Test
    void sprite_intersects_noCollision() throws IOException {
        SpriteObject sprite1 = new SpriteObject(testImageFile.getAbsolutePath(), 0, 0);    // 100x50 em (0,0)
        SpriteObject sprite2 = new SpriteObject(testImageFile.getAbsolutePath(), 200, 100); // 100x50 em (200,100)
        
        assertFalse(sprite1.intersects(sprite2), "Sprites não devem se intersectar");
        assertFalse(sprite2.intersects(sprite1), "Não-interseção deve ser simétrica");
    }

    @Test
    void sprite_render_withNullGraphics_doesNotThrow() throws IOException {
        SpriteObject sprite = new SpriteObject(testImageFile.getAbsolutePath(), 0, 0);
        
        assertDoesNotThrow(() -> sprite.render(null), "Render com Graphics null não deve lançar exceção");
    }

    @Test
    void sprite_render_withNullImage_doesNotThrow() {
        BufferedImage image = new BufferedImage(10, 10, BufferedImage.TYPE_INT_ARGB);
        SpriteObject sprite = new SpriteObject(image, 0, 0);
        sprite.setImage((BufferedImage) null);
        
        BufferedImage canvas = new BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = canvas.createGraphics();
        
        assertDoesNotThrow(() -> sprite.render(g), "Render com imagem null não deve lançar exceção");
        
        g.dispose();
    }

    @Test
    void sprite_inheritsGameObjectBehavior() throws IOException {
        SpriteObject sprite = new SpriteObject(testImageFile.getAbsolutePath(), 0, 0);
        
        // Testa gravidade
        sprite.setHasGravity(true);
        assertTrue(sprite.getHasGravity(), "Deve ter gravidade");
        
        // Testa velocidade
        Vector2 speed = new Vector2(10, 20);
        sprite.setSpeed(speed);
        assertEquals(10.0, sprite.getSpeed().getX(), DELTA, "Velocidade X deve ser 10");
        assertEquals(20.0, sprite.getSpeed().getY(), DELTA, "Velocidade Y deve ser 20");
        
        // Testa update
        Vector2 initialPos = new Vector2(sprite.getPosition().getX(), sprite.getPosition().getY());
        sprite.update(1.0); // 1 segundo
        
        // Com velocidade (10, 20) e gravidade, posição deve mudar
        assertNotEquals(initialPos.getX(), sprite.getPosition().getX(), DELTA, "Posição X deve ter mudado");
        assertNotEquals(initialPos.getY(), sprite.getPosition().getY(), DELTA, "Posição Y deve ter mudado");
    }

    @Test
    void sprite_setImageFromSubImage_withBufferedImage_extractsCorrectRegion() throws IOException {
        // Cria uma imagem de 100x100 para usar como spritesheet
        BufferedImage spritesheet = new BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = spritesheet.createGraphics();
        g.fillRect(0, 0, 100, 100);
        g.dispose();
        
        SpriteObject sprite = new SpriteObject(testImageFile.getAbsolutePath(), 0, 0);
        
        // Extrai uma região de 32x32 começando em (10, 20)
        sprite.setImageFromSubImage(spritesheet, 10, 20, 32, 32);
        
        assertNotNull(sprite.getImage(), "Imagem deve estar definida");
        assertEquals(32, sprite.getWidth(), "Largura deve ser 32");
        assertEquals(32, sprite.getHeight(), "Altura deve ser 32");
        assertEquals(32, sprite.getImage().getWidth(), "Largura da imagem deve ser 32");
        assertEquals(32, sprite.getImage().getHeight(), "Altura da imagem deve ser 32");
    }

    @Test
    void sprite_setImageFromSubImage_withFilePath_extractsCorrectRegion() throws IOException {
        SpriteObject sprite = new SpriteObject(testImageFile.getAbsolutePath(), 0, 0);
        
        // Extrai uma região de 50x25 começando em (0, 0) da imagem de teste (100x50)
        sprite.setImageFromSubImage(testImageFile.getAbsolutePath(), 0, 0, 50, 25);
        
        assertNotNull(sprite.getImage(), "Imagem deve estar definida");
        assertEquals(50, sprite.getWidth(), "Largura deve ser 50");
        assertEquals(25, sprite.getHeight(), "Altura deve ser 25");
    }

    @Test
    void sprite_setImageFromSubImage_withFullImage_worksCorrectly() throws IOException {
        SpriteObject sprite = new SpriteObject(testImageFile.getAbsolutePath(), 0, 0);
        
        // Extrai a imagem completa (100x50)
        sprite.setImageFromSubImage(testImageFile.getAbsolutePath(), 0, 0, 100, 50);
        
        assertNotNull(sprite.getImage(), "Imagem deve estar definida");
        assertEquals(100, sprite.getWidth(), "Largura deve ser 100");
        assertEquals(50, sprite.getHeight(), "Altura deve ser 50");
    }

    @Test
    void sprite_setImageFromSubImage_withNullImage_throwsException() throws IOException {
        SpriteObject sprite = new SpriteObject(testImageFile.getAbsolutePath(), 0, 0);
        
        assertThrows(IllegalArgumentException.class, () -> {
            sprite.setImageFromSubImage((BufferedImage) null, 0, 0, 10, 10);
        }, "Deve lançar IllegalArgumentException para imagem nula");
    }

    @Test
    void sprite_setImageFromSubImage_withNegativeCoordinates_throwsException() throws IOException {
        BufferedImage image = new BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB);
        SpriteObject sprite = new SpriteObject(testImageFile.getAbsolutePath(), 0, 0);
        
        assertThrows(IllegalArgumentException.class, () -> {
            sprite.setImageFromSubImage(image, -10, 0, 10, 10);
        }, "Deve lançar exceção para X negativo");
        
        assertThrows(IllegalArgumentException.class, () -> {
            sprite.setImageFromSubImage(image, 0, -10, 10, 10);
        }, "Deve lançar exceção para Y negativo");
    }

    @Test
    void sprite_setImageFromSubImage_withInvalidDimensions_throwsException() throws IOException {
        BufferedImage image = new BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB);
        SpriteObject sprite = new SpriteObject(testImageFile.getAbsolutePath(), 0, 0);
        
        assertThrows(IllegalArgumentException.class, () -> {
            sprite.setImageFromSubImage(image, 0, 0, 0, 10);
        }, "Deve lançar exceção para largura zero");
        
        assertThrows(IllegalArgumentException.class, () -> {
            sprite.setImageFromSubImage(image, 0, 0, 10, 0);
        }, "Deve lançar exceção para altura zero");
        
        assertThrows(IllegalArgumentException.class, () -> {
            sprite.setImageFromSubImage(image, 0, 0, -10, 10);
        }, "Deve lançar exceção para largura negativa");
        
        assertThrows(IllegalArgumentException.class, () -> {
            sprite.setImageFromSubImage(image, 0, 0, 10, -10);
        }, "Deve lançar exceção para altura negativa");
    }

    @Test
    void sprite_setImageFromSubImage_outOfBounds_throwsException() throws IOException {
        BufferedImage image = new BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB);
        SpriteObject sprite = new SpriteObject(testImageFile.getAbsolutePath(), 0, 0);
        
        assertThrows(IllegalArgumentException.class, () -> {
            sprite.setImageFromSubImage(image, 90, 0, 20, 10); // 90 + 20 > 100
        }, "Deve lançar exceção quando região ultrapassa largura");
        
        assertThrows(IllegalArgumentException.class, () -> {
            sprite.setImageFromSubImage(image, 0, 90, 10, 20); // 90 + 20 > 100
        }, "Deve lançar exceção quando região ultrapassa altura");
        
        assertThrows(IllegalArgumentException.class, () -> {
            sprite.setImageFromSubImage(image, 100, 100, 10, 10); // Começa fora da imagem
        }, "Deve lançar exceção quando região começa fora da imagem");
    }

    @Test
    void sprite_setImageFromSubImage_withInvalidFilePath_throwsIOException() throws IOException {
        SpriteObject sprite = new SpriteObject(testImageFile.getAbsolutePath(), 0, 0);
        
        assertThrows(IOException.class, () -> {
            sprite.setImageFromSubImage("/caminho/invalido/imagem.png", 0, 0, 10, 10);
        }, "Deve lançar IOException para caminho inválido");
    }

    @Test
    void sprite_setImageFromSubImage_multipleExtractions_worksCorrectly() throws IOException {
        // Cria uma spritesheet de 128x64 com múltiplos tiles de 32x32
        BufferedImage spritesheet = new BufferedImage(128, 64, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = spritesheet.createGraphics();
        g.fillRect(0, 0, 128, 64);
        g.dispose();
        
        SpriteObject sprite = new SpriteObject(testImageFile.getAbsolutePath(), 0, 0);
        
        // Extrai primeiro tile (0, 0, 32, 32)
        sprite.setImageFromSubImage(spritesheet, 0, 0, 32, 32);
        assertEquals(32, sprite.getWidth(), "Primeira extração: largura deve ser 32");
        assertEquals(32, sprite.getHeight(), "Primeira extração: altura deve ser 32");
        
        // Extrai segundo tile (32, 0, 32, 32)
        sprite.setImageFromSubImage(spritesheet, 32, 0, 32, 32);
        assertEquals(32, sprite.getWidth(), "Segunda extração: largura deve ser 32");
        assertEquals(32, sprite.getHeight(), "Segunda extração: altura deve ser 32");
        
        // Extrai tile de tamanho diferente (64, 0, 64, 32)
        sprite.setImageFromSubImage(spritesheet, 64, 0, 64, 32);
        assertEquals(64, sprite.getWidth(), "Terceira extração: largura deve ser 64");
        assertEquals(32, sprite.getHeight(), "Terceira extração: altura deve ser 32");
    }

    @Test
    void sprite_setImageFromSubImage_preservesScale() throws IOException {
        BufferedImage spritesheet = new BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB);
        SpriteObject sprite = new SpriteObject(testImageFile.getAbsolutePath(), 0, 0);
        
        // Define escala antes de extrair subimagem
        sprite.setScale(2.0f);
        sprite.setImageFromSubImage(spritesheet, 0, 0, 32, 32);
        
        // A escala deve ser aplicada ao tamanho final
        assertEquals(64, sprite.getWidth(), "Largura com escala deve ser 64 (32 * 2)");
        assertEquals(64, sprite.getHeight(), "Altura com escala deve ser 64 (32 * 2)");
    }

    @Test
    void sprite_setImageFromSubImage_withKeepSize_preservesDimensions() throws IOException {
        BufferedImage spritesheet = new BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB);
        SpriteObject sprite = new SpriteObject(testImageFile.getAbsolutePath(), 0, 0);
        sprite.setSize(64, 64); // Define tamanho customizado
        
        // Extrai subimagem mas mantém o tamanho atual
        sprite.setImageFromSubImage(spritesheet, 0, 0, 32, 32, true);
        
        assertNotNull(sprite.getImage(), "Imagem deve estar definida");
        assertEquals(64, sprite.getWidth(), "Largura deve permanecer 64");
        assertEquals(64, sprite.getHeight(), "Altura deve permanecer 64");
        assertEquals(32, sprite.getImage().getWidth(), "Imagem extraída deve ter 32 de largura");
        assertEquals(32, sprite.getImage().getHeight(), "Imagem extraída deve ter 32 de altura");
    }

    @Test
    void sprite_setImageFromSubImage_withoutKeepSize_updatesDimensions() throws IOException {
        BufferedImage spritesheet = new BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB);
        SpriteObject sprite = new SpriteObject(testImageFile.getAbsolutePath(), 0, 0);
        sprite.setSize(64, 64); // Define tamanho customizado
        
        // Extrai subimagem e atualiza o tamanho
        sprite.setImageFromSubImage(spritesheet, 0, 0, 32, 32, false);
        
        assertNotNull(sprite.getImage(), "Imagem deve estar definida");
        assertEquals(32, sprite.getWidth(), "Largura deve ser atualizada para 32");
        assertEquals(32, sprite.getHeight(), "Altura deve ser atualizada para 32");
    }

    @Test
    void sprite_setImageFromSubImage_filePath_withKeepSize_preservesDimensions() throws IOException {
        SpriteObject sprite = new SpriteObject(testImageFile.getAbsolutePath(), 0, 0);
        sprite.setSize(80, 80); // Define tamanho customizado
        
        // Extrai subimagem mas mantém o tamanho atual
        sprite.setImageFromSubImage(testImageFile.getAbsolutePath(), 0, 0, 50, 25, true);
        
        assertNotNull(sprite.getImage(), "Imagem deve estar definida");
        assertEquals(80, sprite.getWidth(), "Largura deve permanecer 80");
        assertEquals(80, sprite.getHeight(), "Altura deve permanecer 80");
    }

    @Test
    void sprite_setImageFromSubImage_filePath_withoutKeepSize_updatesDimensions() throws IOException {
        SpriteObject sprite = new SpriteObject(testImageFile.getAbsolutePath(), 0, 0);
        sprite.setSize(80, 80); // Define tamanho customizado
        
        // Extrai subimagem e atualiza o tamanho
        sprite.setImageFromSubImage(testImageFile.getAbsolutePath(), 0, 0, 50, 25, false);
        
        assertNotNull(sprite.getImage(), "Imagem deve estar definida");
        assertEquals(50, sprite.getWidth(), "Largura deve ser atualizada para 50");
        assertEquals(25, sprite.getHeight(), "Altura deve ser atualizada para 25");
    }
}
