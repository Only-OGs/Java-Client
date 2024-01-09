package Rendering;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * Rendert die 3 Hintergrund Layer wird von der Render Klasse aufgerufen.
 */
public class BackgroundRenderer {
    public static void renderBackground(SpriteBatch batch,float skyOffset,float hillOffset,float treeOffset, int playerY,float resolution){
        batch.begin();
        renderLayer(batch, AssetData.getBackground(0), skyOffset, (int) (playerY * 0.001f * resolution));
        renderLayer(batch, AssetData.getBackground(1), hillOffset, (int) (playerY * 0.002f * resolution));
        renderLayer(batch, AssetData.getBackground(2), treeOffset, (int) (playerY * 0.003f * resolution));
        batch.end();
    }
    private static void renderLayer(SpriteBatch sB, Texture txt, float offset, int lift) {
        int sourceX = (int) Math.floor(txt.getWidth() * offset);
        int sourceW = Math.min(txt.getWidth(), txt.getWidth() - sourceX);

        int destW = (int) Math.floor(Gdx.graphics.getWidth() * (sourceW / (txt.getWidth() / 2f)));

        sB.draw(txt, 0, lift, destW, Gdx.graphics.getHeight(), sourceX, 0, sourceW, txt.getHeight(), false, false);
        if (sourceW < txt.getWidth()) {
            sB.draw(txt, destW - 1, lift, Gdx.graphics.getWidth() - destW, Gdx.graphics.getHeight(), 0, 0, txt.getWidth() / 2 - sourceW, txt.getHeight(), false, false);
        }
    }
}
