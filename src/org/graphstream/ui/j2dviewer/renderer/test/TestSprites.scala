package org.graphstream.ui.j2dviewer.renderer.test

import org.graphstream.graph.{Graph, Edge}
import org.graphstream.scalags.graph.MultiGraph

import org.graphstream.algorithm.Toolkit._

import org.graphstream.ui.graphicGraph.stylesheet.{Values, StyleConstants}
import org.graphstream.ui.swingViewer.{Viewer, DefaultView, ViewerPipe, ViewerListener}
import org.graphstream.ui.graphicGraph.stylesheet.StyleConstants
import org.graphstream.ui.spriteManager._
import org.graphstream.ui.j2dviewer._

import org.graphstream.ScalaGS._

object TestSprites {
	def main( args:Array[String] ) {
		(new TestSprites).run
	}
}

class TestSprites extends ViewerListener {
	var loop = true
	
	def run() = {
		val graph  = new MultiGraph( "TestSprites" )
		val viewer = new Viewer( graph, Viewer.ThreadingModel.GRAPH_IN_ANOTHER_THREAD )
		val pipeIn = viewer.newViewerPipe
		val view   = viewer.addView( "view1", new J2DGraphRenderer )
  
		pipeIn.addAttributeSink( graph )
		pipeIn.addViewerListener( this )
		pipeIn.pump

		graph.addAttribute( "ui.stylesheet", styleSheet )
		graph.addAttribute( "ui.antialias" )
		graph.addAttribute( "ui.quality" )
		
		val A = graph.addNode( "A" )
		val B = graph.addNode( "B" )
		val C = graph.addNode( "C" )
		val D = graph.addNode( "D" )

		graph.addEdge( "AB1", "A", "B", true )
		graph.addEdge( "AB2", "B", "A", true )
		graph.addEdge( "BC", "B", "C" )
		graph.addEdge( "CD", "C", "D" )
		graph.addEdge( "DA", "D", "A" )
		graph.addEdge( "BB", "B", "B" )
		
		A("xyz") = ( 0, 1, 0 )
		B("xyz") = ( 1.5, 1, 0 )
		C("xyz") = ( 1, 0, 0 )
		D("xyz") = ( 0, 0, 0 )
		
		A("label") = "A"
		B("label") = "B"
		C("label") = "C"
		D("label") = "D"
		
		val sman = new SpriteManager( graph )
 
		sman.setSpriteFactory( new MySpriteFactory )
		
		val s1 = sman.addSprite( "S1" ).asInstanceOf[MySprite]
			
		s1.attachToEdge( "AB1" )
		
		while( loop ) {
			pipeIn.pump
			s1.move
			sleep( 10 )
		}
		
		printf( "bye bye" )
		exit
	}
	
	protected def sleep( ms:Long ) { Thread.sleep( ms ) }

// Viewer Listener Interface
 
	def viewClosed( id:String ) { loop = false }
 
 	def buttonPushed( id:String ) {
 		if( id == "quit" )
 			loop = false
 		else if( id == "A" )
 			print( "Button A pushed%n".format() )
 	}
  
 	def buttonReleased( id:String ) {} 
 
// Data
 	
	val styleSheet = """
			graph {
				canvas-color: white;
 				fill-mode: gradient-radial;
 				fill-color: white, #EEEEEE;
 				padding: 60px;
 			} 
			node {
				shape: circle;
				size: 14px;
				fill-mode: plain;
				fill-color: white;
				stroke-mode: plain; 
				stroke-color: grey;
				stroke-width: 1px;
			}
			node:clicked {
				stroke-mode: plain;
				stroke-color: red;
			}
			node:selected {
				stroke-mode: plain;
				stroke-color: blue;
			}
			edge {
				shape: line;
				size: 1px;
				fill-color: grey;
				fill-mode: plain;
				arrow-shape: arrow;
				arrow-size: 10px, 3px;
			}
			edge#BC {
				shape: cubic-curve;
			}
			sprite {
				shape: circle;
				fill-color: red;
			}
			"""

	class MySpriteFactory extends SpriteFactory {
		override def newSprite( identifier:String, manager:SpriteManager, position:Values ):Sprite = {
			if( position != null )
				return new MySprite( identifier, manager, position );
		
			return new MySprite( identifier, manager );
		}
	}
	
	class MySprite( identifier:String, manager:SpriteManager, pos:Values ) extends Sprite( identifier, manager, pos ) {
		def this( identifier:String, manager:SpriteManager ) {
			this( identifier, manager, new Values( StyleConstants.Units.GU, 0, 0, 0 ) )
		}
		
		val SPEED = 0.005f
		var speed = SPEED
		
		def move() {
			var p = getX
			
			p += speed
			
			if( p < 0 || p > 1 ) {
				val edge = getAttachment.asInstanceOf[Edge]
				val node = if( p > 1 ) edge.getTargetNode else edge.getSourceNode
				var other = randomOutEdge( node )
				
				if( node.getOutDegree > 1 ) { while( other eq edge ) other = randomOutEdge( node ) }
				
				attachToEdge( other.getId )
				if( node eq other.getSourceNode ) {
					setPosition( 0 )
					speed = SPEED
				} else {
					setPosition( 1 )
					speed = -SPEED
				}
			} else {
				setPosition( p )
			}
		}
	}
}