package app.core.action.image;

import linq.ArrayList;
import app.core.entitymodels.ImageInfo;

public interface IActionImage {

	public ArrayList<ImageInfo> setImageData();

	public boolean addImage(ImageInfo imageToAdd);

	public boolean updateImageInfo(ImageInfo updatedImage);

	public boolean removeImage(ImageInfo imageToRemove);
	
	
}
