package Screens;

import Connection.Client;
import MathHelpers.Util;
import Rendering.RenderSegment;
import Rendering.TextureRegionBuilder;
import Road.RoadBuilder;
import Road.Segment;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import org.json.JSONException;

public class GameScreen extends ScreenAdapter {

    private static Camera camera;
    private static Viewport viewport;

    public static TextureRegionBuilder builder = new TextureRegionBuilder();

    private SpriteBatch batch;
    private Texture background;
    ShapeRenderer renderer;
    private int backgroundWitdh = 500;

    private int backgroundHeight = 500;

    //TEST Variables

    private Segment[] segments;
    private int roadWidth = 2000;
    private int segmentLenght = 200;
    private int lanes = 3;
    private int segmentsCount=200;
    private int trackLenght;
    private int FOV = 100;
    private int cameraHeight = 500;
    private float cameraDepth;
    private int drawDistance = 100;
    private float playerX = 0;
    private float playerZ;
    private double cameraPosition = 0;


    public GameScreen() {
        camera = new OrthographicCamera();
        viewport = new ScreenViewport();
        batch = new SpriteBatch();
        background = new Texture("background/hills.png");
        renderer = new ShapeRenderer();
        segments = RoadBuilder.resetRoad(segmentsCount,segmentLenght);
        trackLenght = segments.length*segmentLenght;
        cameraDepth = (float) (1/Math.tan((FOV/2)*Math.PI/180));

    }


    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0, 1);
        renderSegments(renderer);
        double result = cameraPosition + 100;
        while (result >= trackLenght)
            result -= trackLenght;
        while (result < 0)
            result += trackLenght;
        cameraPosition=result;

        if(Client.socket.connected()){
            try {
                Client.emitCoordinate(1,1,1);
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public void resize(int width, int height) {
    }

    @Override
    public void dispose () {
        batch.dispose();
        background.dispose();
    }

    public void renderSegments(ShapeRenderer r){
        Segment baseSegment = findSegment(cameraPosition);
        int maxy = Gdx.graphics.getHeight();

        Segment segment;
        int segmentLoopedValue;
        for (int i=0;i<drawDistance;i++){
            segment = segments[(baseSegment.getIndex()+i)%segments.length];
            segment.setLooped(segment.getIndex()<baseSegment.getIndex());

            if(segment.isLooped()){
                segmentLoopedValue=trackLenght;
            }else{
                segmentLoopedValue=0;
            }

            Util.project(segment.getP1(),(int)(playerX*roadWidth),cameraHeight,(int)cameraPosition-segmentLoopedValue,cameraDepth,Gdx.graphics.getWidth(), Gdx.graphics.getHeight(),roadWidth);
            Util.project(segment.getP2(),(int)(playerX*roadWidth),cameraHeight,(int)cameraPosition-segmentLoopedValue,cameraDepth,Gdx.graphics.getWidth(), Gdx.graphics.getHeight(),roadWidth);

            System.out.println(segment.getIndex());
            if((segment.getP1().getCamera().getZ() <= cameraDepth) || (segment.getP2().getScreen().getY()>=maxy)){
                continue;
            }
            System.out.println("Drawn");
            RenderSegment.render(r,Gdx.graphics.getWidth(),lanes,
                    segment.getP1().getScreen().getX(),
                    segment.getP1().getScreen().getY(),
                    segment.getP1().getScreen().getW(),
                    segment.getP2().getScreen().getX(),
                    segment.getP2().getScreen().getY(),
                    segment.getP2().getScreen().getW(),
                    0,
                    segment.getColor()
            );
            maxy=segment.getP2().getScreen().getY();
        }
    }

    private Segment findSegment(double p){
        return segments[(int) (Math.floor(p/segmentLenght) % segments.length)];
    }
}
