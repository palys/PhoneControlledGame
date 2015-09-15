package game;

import java.io.Serializable;

public interface Message extends Serializable {

	<T extends Serializable> T getContent();
}
