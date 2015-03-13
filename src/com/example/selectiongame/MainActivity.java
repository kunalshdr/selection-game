// Assignment: Homework 1
// MainActivity.java 
// Kunal Shridhar, Vinmay Nair, Swapnil Kadam 


package com.example.selectiongame;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends Activity {

	GridLayout imageLayout;
	TextView fruitAndNumberTextView;
	String focusFruit;
	int counter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		imageLayout = (GridLayout) findViewById(R.id.imageLayout);

		// programatically create 5x5 image view grid
		createImageGrid();

		// exit button event
		findViewById(R.id.exitButton).setOnClickListener(
				new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						endGame();
					}
				});

		// reset button event
		findViewById(R.id.resetButton).setOnClickListener(
				new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						createNewGame();
					}
				});
	}

	// 5x5 image view grid
	private void createImageGrid() {

		// number of fruits of a particular type to select
		int numOfFruitsToSelect = getRandom(1, 8);

		// get random number between 1 and 6; each number denotes a fruit
		int fruitToSelect = getRandom(1, 6);

		// mapping random number to fruits to find focus fruit
		focusFruit = getRandomFruit(fruitToSelect);

		counter = numOfFruitsToSelect;

		fruitAndNumberTextView = (TextView) findViewById(R.id.fruitAndNumberTextView);

		for (int i = 0; i < 25; i++) {
			ImageView imageView = new ImageView(this);
			imageView.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					if (v.getTag().toString().equalsIgnoreCase(focusFruit)
							&& (int) v.getAlpha() == 1) {
						v.setAlpha(0.4f);
						counter--;
						fruitAndNumberTextView.setText(" " + focusFruit + " ("
								+ counter + ")");

						if (counter > 0)
							reShuffleFruits();
						else if (counter == 0) {
							displayWinningAlert();
							lockGrid();
						}
					} else if ((int) v.getAlpha() < 1) {
						// do nothing, as fruit is already selected 
					}	else {
						displayLosingAlert();
					}
				}
			});
			imageLayout.addView(imageView, i);
		}

		// set the fruit to find and number of fruits to find at top
		String gameAim = " " + focusFruit + " (" + numOfFruitsToSelect + ")";
		fruitAndNumberTextView.setText(gameAim);

		// place focus fruit and other fruits on grid
		placeFruitsOnGrid(focusFruit.toLowerCase(), numOfFruitsToSelect);
	}

	// disable all the grid images once game is won
	protected void lockGrid() {
		for (int index = 0; index < 25; index++) {
			imageLayout.getChildAt(index).setClickable(false);
		}
	}

	// alert for losing the game
	protected void displayLosingAlert() {
		AlertDialog.Builder loseAlert = new AlertDialog.Builder(this);
		loseAlert.setTitle("Game Over !!");
		loseAlert.setMessage("You picked the wrong fruit !!");
		loseAlert.setNegativeButton("Exit",
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						endGame();
					}
				});

		loseAlert.setPositiveButton("Reset",
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						createNewGame();
					}
				});

		// do not allow click event outside dialog
		AlertDialog loseDialog = loseAlert.create();
		loseDialog.setCanceledOnTouchOutside(false);
		loseDialog.show();
	}

	// alert when game is won
	protected void displayWinningAlert() {
		AlertDialog.Builder winAlert = new AlertDialog.Builder(this);
		winAlert.setTitle("Found all shapes");
		winAlert.setMessage("Congratulations !! You have found all the "
				+ focusFruit + "s!!");

		winAlert.setNegativeButton("OK", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
			}
		});

		winAlert.setPositiveButton("New Game",
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						createNewGame();
					}
				});

		// do not allow click event outside dialog
		AlertDialog winDialog = winAlert.create();
		winDialog.setCanceledOnTouchOutside(false);
		winDialog.show();
	}

	// exit from the game
	protected void endGame() {
		finish();
		System.exit(0);
	}

	// re-create the game board
	protected void createNewGame() {
		fruitAndNumberTextView.setText("");
		imageLayout.removeAllViews();
		focusFruit = "";
		createImageGrid();
	}

	// shuffle the grid randomly
	// the logic takes 2 random grid locations and swap them
	// this process is repeated 20 time to increase randomness
	protected void reShuffleFruits() {
		for (int i = 0; i < 20;) {
			ImageView v1 = (ImageView) imageLayout.getChildAt(getRandom(0, 24));
			ImageView v2 = (ImageView) imageLayout.getChildAt(getRandom(0, 24));

			// Leave alone already selected images
			if (!(v1.getAlpha() < 1 || v2.getAlpha() < 1)) {
				String firstTag = v1.getTag().toString();

				// need to update tags also for next iterations
				v1.setTag(v2.getTag());
				v1.setImageResource(getFruitId(v2.getTag().toString()));

				v2.setTag(firstTag);
				v2.setImageResource(getFruitId(firstTag));

				i++;
			}
		}
	}

	// place all the fruits on the grid
	private void placeFruitsOnGrid(String fruitName, int N) {

		// place the focus fruit first
		for (int i = 0; i < N;) {
			// get random position on the grid to place fruit
			int randomPosition = getRandom(0, 24);
			ImageView v = (ImageView) imageLayout.getChildAt(randomPosition);

			Log.d("ImageView Null Check", v.toString());

			// If not null, then image already exists at that position
			if (v.getDrawable() == null) {
				v.setTag(fruitName);
				v.setPadding(15, 15, 15, 15);
				v.setImageResource(getFruitId(fruitName));
				i++;
			}

		}

		// place all other items
		for (int i = 0; i < 25 - N;) {
			// get random position on the grid to place fruit
			int randomPosition = getRandom(0, 24);
			ImageView v = (ImageView) imageLayout.getChildAt(randomPosition);

			Log.d("ImageView Null Check", v.toString());

			// If not null, then image already exists at that position
			if (v.getDrawable() == null) {
				String randomFruit = getRandomFruitStr(fruitName);
				v.setTag(randomFruit);
				v.setPadding(10, 10, 10, 10);
				v.setImageResource(getFruitId(randomFruit));
				i++;
			}
		}
	}

	private String getRandomFruitStr(String fruitName) {
		int fruitNum = getRandom(1, 6);
		String randomFruit = getRandomFruit(fruitNum);

		// to make sure that focus fruit is not selected while populating rest
		// of the grid
		while (randomFruit.toLowerCase().equals(fruitName)) {
			randomFruit = getRandomFruit(getRandom(1, 6));
		}

		return randomFruit;
	}

	// return id of image of focus fruit
	private int getFruitId(String fruitName) {
		if (fruitName.equalsIgnoreCase("apple")) {
			return R.drawable.apple;
		} else if (fruitName.equalsIgnoreCase("mango")) {
			return R.drawable.mango;
		} else if (fruitName.equalsIgnoreCase("lemon")) {
			return R.drawable.lemon;
		} else if (fruitName.equalsIgnoreCase("peach")) {
			return R.drawable.peach;
		} else if (fruitName.equalsIgnoreCase("strawberry")) {
			return R.drawable.strawberry;
		} else if (fruitName.equalsIgnoreCase("tomato")) {
			return R.drawable.tomato;
		}
		return 0;
	}

	// return the fruit to find
	private String getRandomFruit(int fruitToSelect) {
		String focussedFruit = "";

		if (fruitToSelect == 1) {
			focussedFruit = "Apple";
		} else if (fruitToSelect == 2) {
			focussedFruit = "Mango";
		} else if (fruitToSelect == 3) {
			focussedFruit = "Peach";
		} else if (fruitToSelect == 4) {
			focussedFruit = "Lemon";
		} else if (fruitToSelect == 5) {
			focussedFruit = "Strawberry";
		} else if (fruitToSelect == 6) {
			focussedFruit = "Tomato";
		}

		return focussedFruit;
	}

	// return a random number between a and b
	private int getRandom(int a, int b) {
		return (int) ((b - a + 1) * Math.random() + a);
	}

}
