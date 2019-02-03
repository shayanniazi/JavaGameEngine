package Components;

import MathUtil.Vector2f;
import MathUtil.Vector3f;
import CoreEngine.DisplayManager;
import Input.Input;

public class FreeLook extends GameComponent
{
	private static final Vector3f yAxis = new Vector3f(0,1,0);

	private boolean mouseLocked = false;
	private float sensitivity;
	private int unlockMouseKey;

	public FreeLook(float sensitivity)
	{
		this(sensitivity, Input.KEY_ESCAPE);
	}

	public FreeLook(float sensitivity, int unlockMouseKey)
	{
		this.sensitivity = sensitivity;
		this.unlockMouseKey = unlockMouseKey;
	}

	@Override
	public void input(float delta)
	{
		Vector2f centerPosition = new Vector2f(DisplayManager.get_width()/2, DisplayManager.get_height()/2);

		if(Input.GetKey(unlockMouseKey))
		{
			Input.SetCursor(true);
			mouseLocked = false;
		}
		if(Input.GetMouseDown(0))
		{
			Input.SetMousePosition(centerPosition);
			Input.SetCursor(false);
			mouseLocked = true;
		}

		if(mouseLocked)
		{
			Vector2f deltaPos = Input.GetMousePosition().Sub(centerPosition);

			boolean rotY = deltaPos.GetX() != 0;
			boolean rotX = deltaPos.GetY() != 0;

			if(rotY)
				get_transform().rotate(yAxis, (float) Math.toRadians(deltaPos.GetX() * sensitivity));
			if(rotX)
				get_transform().rotate(get_transform().get_rotation().getRight(), (float) Math.toRadians(-deltaPos.GetY() * sensitivity));

			if(rotY || rotX)
				Input.SetMousePosition(centerPosition);
		}
	}
}