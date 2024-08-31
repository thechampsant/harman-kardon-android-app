package app.core.model;

import app.core.entitymodels.ImageInfo;
import linq.ArrayList;

public class ImageCaptureResult {

	int OfflineImagesCount = 0;
	int OnlineImagesCount = 0;
	boolean IsImageCaptured = false;
	boolean IsImageUploaded = false;
	boolean IsImageDeleted = false;

	ArrayList<ImageInfo> OfflineImagesInfo = null;
	ArrayList<ImageInfo> OnlineImagesInfo = null;
	private String DocIDs = null;

	public int getTotalImagesCount() {
		return this.OfflineImagesCount + OnlineImagesCount;
	}

	public int getOfflineImagesCount() {
		return this.OfflineImagesCount;
	}

	public int getOnlineImagesCount() {
		return this.OnlineImagesCount;
	}

	public String getDocsIDs() {
		return this.DocIDs;
	}

	public ArrayList<ImageInfo> getOfflineImagesInfo() {
		return OfflineImagesInfo;
	}

	public ArrayList<ImageInfo> getOnlineImagesInfo() {
		return OnlineImagesInfo;
	}

	public int setOfflineImagesCount(int count) {
		return this.OfflineImagesCount;
	}

	public int setOnlineImagesCount(int count) {
		return this.OnlineImagesCount;
	}

	public void setIsImageCaptured(boolean IsImageCaptured) {
		this.IsImageCaptured = IsImageCaptured;
	}

	public void setIsImageUploaded(boolean IsImageUploaded) {
		this.IsImageUploaded = IsImageUploaded;
	}

	public void setIsImageDeleted(boolean IsImageDeleted) {
		this.IsImageDeleted = IsImageDeleted;
	}

	public boolean isImageCaptured() {
		return this.IsImageCaptured;
	}

	public boolean isImageUploaded() {
		return this.IsImageUploaded;
	}

	public boolean isImageDeleted() {
		return this.IsImageDeleted;
	}

	public boolean isAnyImageUploaded() {
		return this.OnlineImagesCount != 0;
	}

	public ImageCaptureResult setResult(ArrayList<ImageInfo> data, String GUID) {
		this.OfflineImagesCount = data.where("HeaderID", GUID)
				.where("IsOffline", "true").Count();
		this.OnlineImagesCount = data.where("HeaderID", GUID)
				.where("IsOffline", "false").Count();

		this.OfflineImagesInfo = data.where("HeaderID", GUID).where(
				"IsOffline", "true");
		this.OnlineImagesInfo = data.where("HeaderID", GUID).where("IsOffline",
				"false");
		setDocsIds();
		return this;
	}

	private void setDocsIds() {
		StringBuilder sb = new StringBuilder();
		for (ImageInfo info : OfflineImagesInfo) {
			sb.append(info.DocID).append(",");
		}
		for (ImageInfo info : OnlineImagesInfo) {
			sb.append(info.DocID).append(",");
		}
		this.DocIDs = sb.toString();
	}
}
