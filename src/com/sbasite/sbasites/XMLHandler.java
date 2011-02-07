package com.sbasite.sbasites;
 
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;


import android.content.Context;
import android.util.Log;
 
public class XMLHandler extends DefaultHandler{
	
	// ===========================================================
	// Fields
	// ===========================================================
	private boolean inTotalRecordCountTag = false;
	private boolean inAGLTag = false;
	private boolean inAddress1Tag = false;
	private boolean inBTATag = false;
	private boolean inCityTag = false;
	private boolean inContactTag = false;
	private boolean inCountyTag = false;
	private boolean inDeletedTag = false;
	private boolean inEmailTag = false;
	private boolean inLastUpdatedTag = false;
	private boolean inLayerTag = false;
	private boolean inLatitudeTag = false;
	private boolean inLongitudeTag = false;
	private boolean inMTATag = false;
	private boolean inMobileKeyTag = false;
	private boolean inPhoneTag = false;
	private boolean inSiteCodeTag = false;
	private boolean inSiteNameTag = false;
	private boolean inSiteStatusTag = false;
	private boolean inStateTag = false;
	private boolean inStructureHeightTag = false;
	private boolean inStructureIDTag = false;
	private boolean inStructureTypeTag = false;
	private boolean inZipTag = false;
	
	private Site mySite;
	public Context context;
	public SBASitesApplication delegate;

	// ===========================================================
	// Methods
	// ===========================================================
	public XMLHandler(Context context) { 
		super();
		this.context = context;
	}
	
	@Override
	public void startDocument() throws SAXException {
		
	}

	@Override
	public void endDocument() throws SAXException {
		// Nothing to do
	}

	/** Gets be called on opening tags like:
	 * <tag>
	 * Can provide attribute(s), when xml was like:
	 * <tag attribute="attributeValue">*/
	@Override
	public void startElement(String namespaceURI, String localName,
			String qName, Attributes atts) throws SAXException {
		if (localName.equals("Site")) {
			this.mySite = new Site(this.context);
		}else if (localName.equals("TotalRecordCount")) {
			this.inTotalRecordCountTag = true;
		}else if (localName.equals("AGL")) {
			this.inAGLTag = true;
		}else if (localName.equals("Address1")) {
			this.inAddress1Tag = true;
		}else if (localName.equals("BTA")) {
			this.inBTATag = true;
		}else if (localName.equals("City")) {
			this.inCityTag = true;
		}else if (localName.equals("Contact")) {
			this.inContactTag = true;
		}else if (localName.equals("County")) {
			this.inCountyTag = true;
		}else if (localName.equals("Deleted")) {
			this.inDeletedTag = true;
		}else if (localName.equals("Email")) {
			this.inEmailTag = true;
		}else if (localName.equals("LastUpdated")) {
			this.inLastUpdatedTag = true;
		}else if (localName.equals("Latitude")) {
			this.inLatitudeTag = true;
		}else if (localName.equals("Layer")) {
			this.inLayerTag = true;
		}else if (localName.equals("Longitude")) {
			this.inLongitudeTag = true;
		}else if (localName.equals("MobileKey")) {
			this.inMobileKeyTag = true;
		}else if (localName.equals("MTA")) {
			this.inMTATag = true;
		}else if (localName.equals("Phone")) {
			this.inPhoneTag = true;
		}else if (localName.equals("SiteCode")) {
			this.inSiteCodeTag = true;
		}else if (localName.equals("SiteName")) {
			this.inSiteNameTag = true;
		}else if (localName.equals("SiteStatus")) {
			this.inSiteStatusTag = true;
		}else if (localName.equals("State")) {
			this.inStateTag = true;
		}else if (localName.equals("StructureHeight")) {
			this.inStructureHeightTag = true;
		}else if (localName.equals("StructureID")) {
			this.inStructureIDTag = true;
		}else if (localName.equals("StructureType")) {
			this.inStructureTypeTag = true;
		}else if (localName.equals("Zip")) {
			this.inZipTag = true;
		}
	}

	/** Gets called on closing tags like:
	 * </tag> */
	@Override
	public void endElement(String namespaceURI, String localName, String qName)
	throws SAXException {
		if (localName.equals("Site")) {
			mySite.save();
			delegate.addSite(null);
		}else if (localName.equals("TotalRecordCount")) {
			this.inTotalRecordCountTag = false;
		}else if (localName.equals("AGL")) {
			this.inAGLTag = false;
		}else if (localName.equals("Address1")) {
			this.inAddress1Tag = false;
		}else if (localName.equals("BTA")) {
			this.inBTATag = false;
		}else if (localName.equals("City")) {
			this.inCityTag = false;
		}else if (localName.equals("Contact")) {
			this.inContactTag = false;
		}else if (localName.equals("County")) {
			this.inCountyTag = false;
		}else if (localName.equals("Deleted")) {
			this.inDeletedTag = false;
		}else if (localName.equals("Email")) {
			this.inEmailTag = false;
		}else if (localName.equals("LastUpdated")) {
			this.inLastUpdatedTag = false;
		}else if (localName.equals("Latitude")) {
			this.inLatitudeTag = false;
		}else if (localName.equals("Layer")) {
			this.inLayerTag = false;
		}else if (localName.equals("Longitude")) {
			this.inLongitudeTag = false;
		}else if (localName.equals("MobileKey")) {
			this.inMobileKeyTag = false;
		}else if (localName.equals("MTA")) {
			this.inMTATag = false;
		}else if (localName.equals("Phone")) {
			this.inPhoneTag = false;
		}else if (localName.equals("SiteCode")) {
			this.inSiteCodeTag = false;
		}else if (localName.equals("SiteName")) {
			this.inSiteNameTag = false;
		}else if (localName.equals("SiteStatus")) {
			this.inSiteStatusTag = false;
		}else if (localName.equals("State")) {
			this.inStateTag = false;
		}else if (localName.equals("StructureHeight")) {
			this.inStructureHeightTag = false;
		}else if (localName.equals("StructureID")) {
			this.inStructureIDTag = false;
		}else if (localName.equals("StructureType")) {
			this.inStructureTypeTag = false;
		}else if (localName.equals("Zip")) {
			this.inZipTag = false;
		}
	}

	/** Gets be called on the following structure:
	 * <tag>characters</tag> */
	@Override
	public void characters(char ch[], int start, int length) {
		Log.v("XMLHandler", (new String(ch, start, length)));
		if(this.inTotalRecordCountTag){
			delegate.totalRecordsCount = Integer.parseInt(new String(ch, start, length));
		}else if(this.inAddress1Tag){
			//mySite.address = new String(ch, start, length);
		}else if(this.inAGLTag){
			//mySite.agl = new String(ch, start, length);
		}else if(this.inBTATag){
			//mySite.bta = new String(ch, start, length);
		}else if(this.inCityTag){
			//mySite.city = new String(ch, start, length);
		}else if(this.inContactTag){
			//mySite.contact = new String(ch, start, length);
		}else if(this.inCountyTag){
			//mySite.county = new String(ch, start, length);
		}else if(this.inDeletedTag){
			//mySite.deleted = Integer.parseInt(new String(ch, start, length));
		}else if(this.inEmailTag){
			//mySite.email = new String(ch, start, length);
		}else if(this.inLastUpdatedTag){
			//mySite.lastUpdated = new String(ch, start, length);
		}else if(this.inLatitudeTag){
			mySite.latitude = Double.parseDouble(new String(ch, start, length));
		}else if(this.inLongitudeTag){
			mySite.longitude = Double.parseDouble(new String(ch, start, length));
		}else if(this.inMobileKeyTag){
			mySite.mobileKey = new String(ch, start, length);
		}else if(this.inMTATag){
			//mySite.mta = new String(ch, start, length);
		}else if(this.inPhoneTag){
			//mySite.phone = new String(ch, start, length);
		}else if(this.inSiteCodeTag){
			mySite.siteCode = new String(ch, start, length);
		}else if(this.inSiteNameTag){
			mySite.siteName = new String(ch, start, length);
		}else if(this.inSiteStatusTag){
			//mySite.siteStatus = new String(ch, start, length);
		}else if(this.inStateTag){
			//mySite.stateProvince = new String(ch, start, length);
		}else if(this.inStructureHeightTag){
			//mySite.structureHeight = new String(ch, start, length);
		}else if(this.inStructureIDTag){
			//mySite.structureID = new String(ch, start, length);
		}else if(this.inStructureTypeTag){
			//mySite.structureType = new String(ch, start, length);
		}else if(this.inZipTag){
			//mySite.zip = new String(ch, start, length);
		}else if(this.inLayerTag){
			//mySite.siteLayer = new String(ch, start, length);
		}
	}
	
	/*
	private SBASitesApplication getSBASitesApplication() {
		SBASitesApplication app = (SBASitesApplication)getApplication();
		return app;
	}
	*/
}