package com.sb.graphic;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

/**
 * Function added for smooth curve
 * 
 * @Sep. 12, 2015
 *
 */
public class Mission09Activity extends ActionBarActivity {

    private Mission09PaintBoard board;

    private Button colorBtn;
    private Button penBtn;
    private Button eraserBtn;
    private RadioButton buttStyleBtn;
    private RadioButton roundStyleBtn;
    private RadioButton squareStyleBtn;

    private Button undoBtn;
    private LinearLayout addedLayout;
    private Button colorLegendBtn;

    private TextView sizeLegendTxt;
    private int mColor = 0xff000000;
    private int mSize = 2;
    private int oldColor;
    private int oldSize;
    private boolean eraserSelected = false;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        LinearLayout toolsLayout = (LinearLayout) findViewById(R.id.toolsLayout);
        final RadioGroup styleGroup= (RadioGroup)findViewById(R.id.styleGroup);
        final LinearLayout boardLayout = (LinearLayout) findViewById(R.id.boardLayout);

        colorBtn = (Button) findViewById(R.id.colorBtn);
        penBtn = (Button) findViewById(R.id.penBtn);
        eraserBtn = (Button) findViewById(R.id.eraserBtn);
        undoBtn = (Button) findViewById(R.id.undoBtn);

        buttStyleBtn = (RadioButton)findViewById(R.id.buttStyleBtn);
        roundStyleBtn = (RadioButton)findViewById(R.id.roundStyleBtn);
        squareStyleBtn = (RadioButton)findViewById(R.id.squareStyleBtn);
        
        // add paint board
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
        		LinearLayout.LayoutParams.MATCH_PARENT,
        		LinearLayout.LayoutParams.MATCH_PARENT);
        
        board = new Mission09PaintBoard(this);
        board.setLayoutParams(params);
        board.setPadding(2, 2, 2, 2);
        
        boardLayout.addView(board);
        
        // add legend buttons
        LinearLayout.LayoutParams addedParams = new LinearLayout.LayoutParams(
        		LinearLayout.LayoutParams.MATCH_PARENT,
        		LinearLayout.LayoutParams.MATCH_PARENT);
        
        LinearLayout.LayoutParams buttonParams = new LinearLayout.LayoutParams(
        		LinearLayout.LayoutParams.MATCH_PARENT,
        		36);
        addedLayout = new LinearLayout(this);
        addedLayout.setLayoutParams(addedParams);
        addedLayout.setOrientation(LinearLayout.VERTICAL);
        addedLayout.setPadding(8, 8, 8, 8);
        
        LinearLayout outlineLayout = new LinearLayout(this);
        outlineLayout.setLayoutParams(buttonParams);
        outlineLayout.setOrientation(LinearLayout.VERTICAL);
        outlineLayout.setBackgroundColor(Color.LTGRAY);
        outlineLayout.setPadding(1, 1, 1, 1);
        
        colorLegendBtn = new Button(this);
        colorLegendBtn.setLayoutParams(buttonParams);
        colorLegendBtn.setText(" ");
        colorLegendBtn.setBackgroundColor(mColor);
        colorLegendBtn.setHeight(20);
        outlineLayout.addView(colorLegendBtn);
        addedLayout.addView(outlineLayout);
        
        sizeLegendTxt = new TextView(this);
        sizeLegendTxt.setLayoutParams(buttonParams);
        sizeLegendTxt.setText("Size : " + mSize);
        sizeLegendTxt.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);
        sizeLegendTxt.setTextSize(16);
        sizeLegendTxt.setTextColor(Color.BLACK);
        addedLayout.addView(sizeLegendTxt);
        
        toolsLayout.addView(addedLayout);

        colorBtn.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {

                ColorPaletteDialog.listener = new OnColorSelectedListener() {
                    public void onColorSelected(int color) {
                        mColor = color;
                        board.updatePaintProperty(mColor, mSize);
                        displayPaintProperty();
                    }
                };


                // show color palette dialog
                Intent intent = new Intent(getApplicationContext(), ColorPaletteDialog.class);
                startActivity(intent);

            }
        });
        
        penBtn.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {

                PenPaletteDialog.listener = new OnPenSelectedListener() {
                    public void onPenSelected(int size) {
                        mSize = size;
                        board.updatePaintProperty(mColor, mSize);
                        displayPaintProperty();
                    }
                };


                // show pen palette dialog
                Intent intent = new Intent(getApplicationContext(), PenPaletteDialog.class);
                startActivity(intent);

            }
        });
        
        eraserBtn.setOnClickListener(new OnClickListener() {
        	public void onClick(View v) {
        		
        		eraserSelected = !eraserSelected;
        		
        		if (eraserSelected) {
                    colorBtn.setEnabled(false);
                    penBtn.setEnabled(false);
                    undoBtn.setEnabled(false);
        			
                    colorBtn.invalidate();
                    penBtn.invalidate();
                    undoBtn.invalidate();
                    
                    oldColor = mColor;
                    oldSize = mSize;
                    
                    mColor = Color.WHITE;
                    mSize = 15;
                    
                    board.updatePaintProperty(mColor, mSize);
                    displayPaintProperty();
                    
                } else {
                	colorBtn.setEnabled(true);
                    penBtn.setEnabled(true);
                    undoBtn.setEnabled(true);
        			
                    colorBtn.invalidate();
                    penBtn.invalidate();
                    undoBtn.invalidate();
                    
                    mColor = oldColor;
                    mSize = oldSize;
                    
                    board.updatePaintProperty(mColor, mSize);
                    displayPaintProperty();
                    
                }
        		
        	}
        });
        
        undoBtn.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {

                board.undo();

            }
        });

        // default setting
        styleGroup.check(R.id.roundStyleBtn);

        styleGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                styleGroup.check(checkedId);
                board.setCapStyle(checkedId);
            }
        });
    }
    
        
    public int getChosenColor() {
    	return mColor;
    }
    
    public int getPenThickness() {
    	return mSize;
    }
    
    private void displayPaintProperty() {
    	colorLegendBtn.setBackgroundColor(mColor);
    	sizeLegendTxt.setText("Size : " + mSize);
    	
    	addedLayout.invalidate();
    }


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu_main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

}