package com.me.screens;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.delay;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.fadeIn;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.fadeOut;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.sequence;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction; 
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.me.mygdxgame.LoadingBar;
import com.me.mygdxgame.MyGdxGame;
import com.siondream.engine.gleed.Level;
import com.siondream.engine.gleed.LevelLoader;

/**
 * @author Mats Svensson
 */
public class LoadingScreen extends AbstractScreen {

    private Stage stage;

    private Image logo;
    private Image loadingFrame;
    private Image loadingBarHidden;
    private Image screenBg;
    private Image loadingBg;

    private float startX, endX;
    private float percent;

    private Actor loadingBar;
    private List<Image> images;

    public LoadingScreen(MyGdxGame game) {
        super(game);
    }

    @Override
    public void show() {
        // Tell the manager to load assets for the loading screen
        game.manager.load("data/loading.pack", TextureAtlas.class);
        // Wait until they are finished loading
        game.manager.finishLoading();

        // Initialize the stage where we will place everything
        stage = new Stage(480,320,true);

        // Get our textureatlas from the manager
        TextureAtlas atlas = game.manager.get("data/loading.pack", TextureAtlas.class);

        // Grab the regions from the atlas and create some images
        logo = new Image(atlas.findRegion("libgdx-logo"));
        loadingFrame = new Image(atlas.findRegion("loading-frame"));
        loadingBarHidden = new Image(atlas.findRegion("loading-bar-hidden"));
        screenBg = new Image(atlas.findRegion("screen-bg"));
        loadingBg = new Image(atlas.findRegion("loading-frame-bg"));

        // Add the loading bar animation
        Animation anim = new Animation(0.05f, atlas.findRegions("loading-bar-anim") );
        anim.setPlayMode(Animation.LOOP_REVERSED);
        loadingBar = new LoadingBar(anim);

        // Or if you only need a static bar, you can do
        // loadingBar = new Image(atlas.findRegion("loading-bar1"));

        // Add all the actors to the stage
        stage.addActor(screenBg);
        stage.addActor(loadingBar);
        stage.addActor(loadingBg);
        stage.addActor(loadingBarHidden);
        stage.addActor(loadingFrame);
        stage.addActor(logo);
        
        images = new ArrayList<Image>();
        images.add(screenBg);
        images.add(loadingBarHidden);
        images.add(loadingBg);
        images.add(logo);
        images.add(loadingFrame);

        // Add everything to be loaded, for instance:
        // game.manager.load("data/assets1.pack", TextureAtlas.class);
        // game.manager.load("data/assets2.pack", TextureAtlas.class);
        // game.manager.load("data/assets3.pack", TextureAtlas.class);
        
  
        
 
        /**
        game.manager.load("cat.png", Texture.class);
        
        game.manager.load("drop.wav", Sound.class);
        
        game.manager.load("droplet.png", Texture.class);
        game.manager.load("bucket.png", Texture.class);
        **/
      
          	// Set loader in the Asset manager
	      game.manager.setLoader(Level.class, new LevelLoader(new InternalFileHandleResolver()));

	      // Tell the manager to load the level
	      game.manager.load("data/TestLevel.xml", Level.class);
     
	       game.manager.load("rain.mp3", Music.class);
        
    }

    @Override
    public void resize(int width, int height) {
        // Set our screen to always be XXX x 480 in size
        width = 480 * width / height;
        height = 480;
        stage.setViewport(width , height, false);

        // Make the background fill the screen
        screenBg.setSize(width, height);

        // Place the logo in the middle of the screen and 100 px up
        logo.setX((width - logo.getWidth()) / 2);
        logo.setY((height - logo.getHeight()) / 2 + 100);

        // Place the loading frame in the middle of the screen
        loadingFrame.setX((stage.getWidth() - loadingFrame.getWidth()) / 2);
        loadingFrame.setY((stage.getHeight() - loadingFrame.getHeight()) / 2);

        // Place the loading bar at the same spot as the frame, adjusted a few px
        loadingBar.setX(loadingFrame.getX() + 15);
        loadingBar.setY(loadingFrame.getY() + 5);

        // Place the image that will hide the bar on top of the bar, adjusted a few px
        loadingBarHidden.setX(loadingBar.getX() + 35);
        loadingBarHidden.setY(loadingBar.getY() - 3);
        // The start position and how far to move the hidden loading bar
        startX = loadingBarHidden.getX();
        endX = 440;

        // The rest of the hidden bar
        loadingBg.setSize(450, 50);
        loadingBg.setX(loadingBarHidden.getX() + 30);
        loadingBg.setY(loadingBarHidden.getY() + 3);
    }

    @Override
    public void render(float delta) {
        // Clear the screen
        Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
        
        boolean doneLoading = game.manager.update();
        float progress = game.manager.getProgress();


        // Interpolate the percentage to make it more smooth
        percent = Interpolation.linear.apply(percent, progress, 1.0f);

        // Update positions (and size) to match the percentage
        loadingBarHidden.setX(startX + endX * percent);
        loadingBg.setX(loadingBarHidden.getX() + 30);
        loadingBg.setWidth(450 - 450 * percent);
        loadingBg.invalidate();

        // Show the loading screen
        stage.act();
        stage.draw();
        
        if (doneLoading ) { // Load some, will return true if done loading
       	 // configure the fade-in/out effect on the splash image
       
        	if ( loadingBar.getActions().size == 0)
        	{
        	
		       	SequenceAction action =  sequence(  fadeOut( 1.75f ),
		                   new Action() {
		               @Override
		               public boolean act(
		                   float delta )
		               {
		                   // the last action will move to the next screen
		               	game.setScreen(new MainMenuScreen(game));
		               	return true;
		               }
		           } );
		   
		       	loadingBar.addAction( action );
		       	
		       	screenBg.addAction(sequence(  fadeOut( 1.75f ),
		                   new Action() {
		               @Override
		               public boolean act(
		                   float delta )
		               {
		                   // the last action will move to the next screen
		               	game.setScreen(new MainMenuScreen(game));
		               	return true;
		               }
		           } ));
		       	
		       	for(int i = 1; i < images.size();++i)
		       	{
		       		images.get(i).addAction(fadeOut( 1.75f ));
		                   	       		
		       	}
		       	
		   }   	
       }
    }

    @Override
    public void hide() {
        // Dispose the loading assets as we no longer need them
        game.manager.unload("data/loading.pack");
    }
}
