package Components;

import Input.Input;
import MathUtil.Vector3f;

public class FreeMove extends GameComponent
{
	private float speed;
	private int forwardKey;
	private int backKey;
	private int leftKey;
	private int rightKey;

	public FreeMove(float speed)
	{
		this(speed, Input.KEY_W, Input.KEY_S, Input.KEY_A, Input.KEY_D);
	}

	public FreeMove(float speed, int forwardKey, int backKey, int leftKey, int rightKey)
	{
		this.speed = speed;
		this.forwardKey = forwardKey;
		this.backKey = backKey;
		this.leftKey = leftKey;
		this.rightKey = rightKey;
	}

	@Override
	public void input(float delta)
	{
		float movAmt = speed * delta;

		if(Input.GetKey(forwardKey))
			move(get_transform().get_rotation().getForward(), movAmt);
		if(Input.GetKey(backKey))
			move(get_transform().get_rotation().getForward(), -movAmt);
		if(Input.GetKey(leftKey))
			move(get_transform().get_rotation().getLeft(), movAmt);
		if(Input.GetKey(rightKey))
			move(get_transform().get_rotation().getRight(), movAmt);
	}

	private void move(Vector3f dir, float amt)
	{
		get_transform().translate(get_transform().get_position().add(dir.mul(amt)));
	}
}