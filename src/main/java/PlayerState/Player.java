package PlayerState;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.imageio.ImageIO;

import EnemyState.Enemy;
import Entity.Entity;
import Skill.Bullet;
import collisionDetection.CreateCollision;
import main.GamePanel;
import main.KeyHandler;

public class Player extends Entity {

	public BufferedImage image;
	// state info
	public PlayerStateMachine stateMachine;
	public PlayerIdleState idleState;
	public PlayerRunState runState;
	public PlayerShootState shootState;
	public PlayerJumpState jumpState;
	public PlayerFallState fallState;
	public PlayerDamagedState damagedState;
	// possition in camera
	public final int screenX;
	public final int screenY;
	// skill
	public int facingGetKnockBack = 1;
	public boolean canKnockBack = true;
	public List<Enemy> enemies;
	private int currentHealth=50;
	// shoot skill
	List<Bullet> projectiles = new ArrayList<Bullet>();

	public Player(GamePanel gp, KeyHandler keyH, List<Enemy> _enemies, boolean _isMy) {
		super(gp, keyH);
		this.enemies = _enemies;
		faceDirection = 1;
		this.isMy = _isMy;
		setDefaulValues();
		getPlayerImage();
		this.stateMachine = new PlayerStateMachine();
		collision = new CreateCollision(this, gp);
		screenX = gp.screenWidth / 2 - gp.tileSize / 2;
		screenY = gp.screenHeight / 2;
		// set box colliders
		width = gp.tileSize * 2;
		height = width;
		solidArea = new Rectangle();
		solidArea.x = 15;
		solidArea.y = 15;
		solidArea.width = 75;
		solidArea.height = 80;
		setUpState();
	}

	public void setUpState() {
		idleState = new PlayerIdleState(this, this.stateMachine, "Idle", gp);
		runState = new PlayerRunState(this, this.stateMachine, "Run", gp);
		shootState = new PlayerShootState(this, this.stateMachine, "Shoot", gp);
		jumpState = new PlayerJumpState(this, stateMachine, "Jump", gp);
		fallState = new PlayerFallState(this, stateMachine, "Fall", gp);
		damagedState = new PlayerDamagedState(this, stateMachine, "Damaged", gp);
		stateMachine.initialize(idleState);
	}

	public void setDefaulValues() {
		Random random = new Random();
		// Tạo số nguyên ngẫu nhiên trong phạm vi từ 22 đến 25
		int randomNumber = random.nextInt(7) + 24;
		System.out.println(randomNumber);
		worldX = randomNumber * gp.tileSize;
		worldY = 18 * gp.tileSize;
		speed = 5;
	}

	@Override
	public void update() {
		manageAnimation();
		for (int i = 0; i < projectiles.size(); i++) {
			Bullet bullet = projectiles.get(i);
			bullet.update();
		}
		if (!isMy)
			return;
		stateMachine.currentState.update();
		updateVelocity();
		collision.updateGravity();
//		for (int i = 0; i < projectiles.size(); i++) {
//			Bullet bullet = projectiles.get(i);
//			bullet.update();
//		}
	}

	public void createBullet() {
		if (gp.mainGameOnlineScene != null) {
			if (isMy && gp.mainGameOnlineScene.playerEnemy != null) {
				Player playerEnemy = gp.mainGameOnlineScene.playerEnemy;
				if (faceDirection == 1)
					projectiles.add(new Bullet(gp, this, playerEnemy, worldX + width - 30,
							worldY + solidArea.height / 2 - 10, faceDirection));
				else {
					projectiles.add(new Bullet(gp, this, playerEnemy, worldX + 30, worldY + solidArea.height / 2 - 10,
							faceDirection));
				}
			}else if(!isMy) {
				Player playerEnemy = gp.mainGameOnlineScene.player;
				if (faceDirection == 1)
					projectiles.add(new Bullet(gp, this, playerEnemy, worldX + width - 30,
							worldY + solidArea.height / 2 - 10, faceDirection));
				else {
					projectiles.add(new Bullet(gp, this, playerEnemy, worldX + 30, worldY + solidArea.height / 2 - 10,
							faceDirection));
				}
			}
			return;
		}
		// ofline
		if (faceDirection == 1)
			projectiles.add(new Bullet(gp, this, enemies, worldX + width - 30, worldY + solidArea.height / 2 - 10,
					faceDirection));
		else {
			projectiles
					.add(new Bullet(gp, this, enemies, worldX + 30, worldY + solidArea.height / 2 - 10, faceDirection));
		}
	}

	private void manageAnimation() {
		if (stateMachine.currentState.animBoolName == "Idle")
			RunAnimation(idle, 3, true);
		if (stateMachine.currentState.animBoolName == "Run")
			RunAnimation(run, 2, true);
		if (stateMachine.currentState.animBoolName == "Shoot")
			RunAnimation(shoot, 3, false);
		if (stateMachine.currentState.animBoolName == "Jump")
			RunAnimation(jump, 1, true);
		if (stateMachine.currentState.animBoolName == "Fall")
			RunAnimation(fall, 1, true);
		if (stateMachine.currentState.animBoolName == "Damaged")
			RunAnimation(damaged, 1, false);
	}

	private void RunAnimation(BufferedImage[] stage, int framerate, boolean isLoop) {
		if (spriteCounter < framerate) {
			spriteCounter++;
		} else {

			if (spriteNum < stage.length) {
				image = stage[spriteNum];
				if (spriteNum + 1 == stage.length && !isLoop) {
					finishedAnimation();
				} else
					spriteNum++;
			} else {
				spriteNum = 0;
				finishedAnimation();
			}
			spriteCounter = 0;
		}
	}

	public void animSetBool(String _animBoolName) {
	}

	public void draw(Graphics2D g2) {
		for (int i = 0; i < projectiles.size(); i++) {
			Bullet bullet = projectiles.get(i);
			if (!bullet.isActive) {
				projectiles.remove(i);
				i--;
			}
			bullet.draw(g2);
		}
		if (isMy) {

			g2.setColor(Color.green);
			// g2.fillRect(screenX+solidArea.x, screenY+solidArea.y,solidArea.width,
			// solidArea.height);
			g2.drawImage(image, (int) (screenX + deltaWhenFlip), screenY, (int) faceDirection * width, height, null);
			// g2.dispose();
		} else {
			// System.out.println(stateMachine.currentState.animBoolName);
			// System.out.println(2);
			g2.setColor(Color.green);
			Player mainPlayer = gp.mainGameOnlineScene.player;
			int screenX1 = worldX - mainPlayer.worldX + mainPlayer.screenX;
			int screenY1 = worldY - mainPlayer.worldY + mainPlayer.screenY;
			if (worldX + width >= mainPlayer.worldX - mainPlayer.screenX
					&& worldX - width <= mainPlayer.worldX + mainPlayer.screenX
					&& worldY + height >= mainPlayer.worldY - mainPlayer.screenY
					&& worldY - height <= mainPlayer.worldY + mainPlayer.screenY) {
				int delta = (faceDirection == -1) ? width : 0;
				g2.drawImage(image, (int) (screenX1 + delta), screenY1, (int) faceDirection * width, height, null);
			}
		}

	}

	public void getPlayerImage() {

	}

	public void finishedAnimation() {
		stateMachine.currentState.triggerCalled = true;
	}

	// facing xac dinh nhan vat bi knock back ve huong nao
	public void getDamaged(int _facingGetKnockBack) {
		currentHealth-=10;
		facingGetKnockBack = _facingGetKnockBack;
		stateMachine.changeState(damagedState);
	}

	public void getDamaged() {
		stateMachine.changeState(damagedState);

	}
	public int getCurrentHealth() {
		return currentHealth;
	}
}
