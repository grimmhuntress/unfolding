//package de.fhpotsdam.unfolding.performance;
//
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.List;
//
//import processing.core.PApplet;
//import processing.core.PVector;
//
//
//
//
//import de.fhpotsdam.unfolding.UnfoldingMap;
//import de.fhpotsdam.unfolding.events.MapEvent;
//import de.fhpotsdam.unfolding.geo.Location;
//import de.fhpotsdam.unfolding.utils.MapUtils;
//
///**
//
// * 
// */
//public class MillionDotsMapApp6GLModelMoving extends PApplet {
//
//	int dotNumber = 20000;
//
//	UnfoldingMap map;
//	// Original dots (loc + time)
//	List<Dot> dots = new ArrayList<Dot>();
//
//	// Visible points
//	List<PVector> visibleDotVertices = new ArrayList<PVector>();
//	// GLGraphics model containing vertices
//	GLModel model;
//
//	GLTexture tex;
//
//	Location tlLoc;
//	Location brLoc;
//
//	public void setup() {
//		size(800, 600, OPENGL);
//		smooth();
//
//		dots = createRandomDots(dotNumber);
//
//		map = new UnfoldingMap(this);
//		map.zoomToLevel(3);
//		MapUtils.createDefaultEventDispatcher(this, map);
//
//		initAllDots();
//		model = new GLModel(this, dotNumber * 4, GLModel.QUADS, GLModel.STATIC);
//
//		model.initTextures(1);
//		tex = new GLTexture(this, "sprite-pink.png");
//		model.setTexture(0, tex);
//		
//		println("visibleDotVertices.size():" + visibleDotVertices.size());
//		updateTexture();
//	}
//
//	public void draw() {
//		background(0);
//		map.draw();
//
//		fill(0, 180);
//		noStroke();
//
//		timeChanged();
//
//		synchronized (visibleDotVertices) {
//			updateModelVertices();
//			drawModel();
//		}
//
//		fill(255);
//		rect(5, 5, 180, 20);
//		fill(0);
//		text("fps: " + nfs(frameRate, 0, 2) + " (" + visibleDotVertices.size() + " dots)", 10, 20);
//	}
//
//	public void drawModel() {
//		GLGraphics renderer = (GLGraphics) g;
//		renderer.beginGL();
//		
//		
//		// Do not draw full model, as only visible dots (i.e. their vertices) should be shown
//		// renderer.model(model);
//		
//		// Using render(int, int, GLEffect) method, due to bug (see
//		// https://forum.processing.org/topic/getting-extra-vertices-when-building-a-mesh-with-glmodel#25080000001046326)
//		int verticesPerSegment = 4;
//		int numVertices = visibleDotVertices.size() * verticesPerSegment;
//		model.render(0, numVertices, null);
//		renderer.endGL();
//	}
//
//	public void updateTexture() {
//		model.beginUpdateTexCoords(0);
//		int verticesPerSegment = 4;
//		int numVertices = visibleDotVertices.size() * verticesPerSegment;
//		for (int i = 0; i < numVertices; i += verticesPerSegment) {
//			model.updateTexCoord(i, 0, 0);
//			model.updateTexCoord(i + 1, 1, 0);
//			model.updateTexCoord(i + 2, 1, 1);
//			model.updateTexCoord(i + 3, 0, 1);
//		}
//		model.endUpdateTexCoords();
//	}
//
//	public void updateModelVertices() {
//		model.beginUpdateVertices();
//		int verticesPerSegment = 4;
//		int numVertices = visibleDotVertices.size() * verticesPerSegment;
//		synchronized (visibleDotVertices) {
//			for (int i = 0; i < numVertices; i += verticesPerSegment) {
//				PVector pos = visibleDotVertices.get(i / verticesPerSegment);
//				model.updateVertex(i, pos.x, pos.y);
//				model.updateVertex(i + 1, pos.x - 4, pos.y);
//				model.updateVertex(i + 2, pos.x - 4, pos.y - 4);
//				model.updateVertex(i + 3, pos.x, pos.y - 4);
//			}
//		}
//		model.endUpdateVertices();
//	}
//
//	public void timeChanged() {
//		mapChanged(null);
//	}
//
//	public void mapChanged(MapEvent mapEvent) {
//		// Check map area only once after user interaction.
//		// Additionally, instead of calculating the screen position each frame, store it in new list.
//		brLoc = map.getBottomRightBorder();
//		tlLoc = map.getTopLeftBorder();
//		synchronized (visibleDotVertices) {
//			visibleDotVertices.clear();
//			for (Dot dot : dots) {
//				Location location = new Location(dot.location.getLat() + random(-1, 1), dot.location.getLon()
//						+ random(-1, 1));
//				if (location.getLat() > brLoc.getLat() && location.getLat() < tlLoc.getLat()
//						&& location.getLon() > tlLoc.getLon() && location.getLon() < brLoc.getLon()) {
//					PVector pos = map.getScreenPosition(location);
//					visibleDotVertices.add(pos);
//				}
//			}
//
//			// model = new GLModel(this, visibleDotVertices.size() * 4, GLModel.QUADS, GLModel.STATIC);
//		}
//	}
//	
//	public void initAllDots() {
//		for (Dot dot : dots) {
//			PVector pos = map.getScreenPosition(dot.location);
//			visibleDotVertices.add(pos);
//		}
//	}
//
//	private List<Dot> createRandomDots(int dotNumbers) {
//		List<Dot> dots = new ArrayList<Dot>();
//		for (int i = 0; i < dotNumbers; i++) {
//			Dot dot = new Dot(new Location(random(-85, 85), random(-180, 180)), new Date());
//			dots.add(dot);
//		}
//		return dots;
//	}
//}
