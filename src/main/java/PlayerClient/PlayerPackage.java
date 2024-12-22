package PlayerClient;

import java.io.Serializable;

public class PlayerPackage implements Serializable {
	private static final long serialVersionUID = 1L;
	public int x, y,faceDir;
	public String animBoolName;

	public PlayerPackage(int x, int y , int _faceDir, String animBoolName) {
		this.x = x;
		this.y = y;
		this.faceDir=_faceDir;
		this.animBoolName = animBoolName;
	}

	@Override
	public String toString() {
		return "PlayerPackage{" +
				"x=" + x +
				", y=" + y +
				", faceDir=" + faceDir +
				", animBoolName='" + animBoolName + '\'' +
				'}';
	}
}
