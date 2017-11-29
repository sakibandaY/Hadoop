package test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import com.investor.Contactpoint;
import com.investor.Electronicaddress;
import com.investor.InverstorAggriate;
import com.investor.Investor;
import com.investor.Organisation;
import com.investor.Organisationidentification;
import com.investor.Organisationname;
import com.investor.Partyidentificationinformation;
import com.investor.Person;
import com.investor.Personidentification;
import com.investor.Personname;
import com.investor.Phoneaddress;
import com.investor.Postaladdress;
import com.reporting.document.Reporting.Document;
import com.reporting.document.Reporting.Document.Recipients;
import com.reporting.document.Reporting.Document.Recipients.Recipient;
import com.reporting.document.Reporting.Document.Recipients.Recipient.Address;

public class BuildRecipentXml {

	public static void build() {

		List<InverstorAggriate> inverstorAggriates = MockServices.mockinv();

	
		List<RecipientUtill> temprecipientUtills = new ArrayList<RecipientUtill>();

		List<RecipientUtill> recipientUtills = new ArrayList<RecipientUtill>();
		
		if (!inverstorAggriates.isEmpty()) {
			InverstorAggriate inverstorAggriate = inverstorAggriates.get(0);
			if (inverstorAggriate != null) {
				if(inverstorAggriate.getPartytype().equalsIgnoreCase("Individual")|| inverstorAggriate.getPartytype().equalsIgnoreCase("Retail")) {
					Person person = inverstorAggriate.getPerson().get(0);
					if(person != null) {
						Personidentification personidentification = person.getPersonidentification().get(0);
						RecipientUtill recipientUtill = new RecipientUtill();
						recipientUtill.setReportingLanguage("en");
						recipientUtill.setChannelCode("PRINT");
						recipientUtill.setCopyType("Investor Old");
						if(personidentification != null) {
							Partyidentificationinformation partyidentificationinformation = personidentification.getPartyidentificationinformation();
							if(partyidentificationinformation != null) 
								recipientUtill.setId(partyidentificationinformation.getGenericidentification().getIdentification());
							
							Personname personname = personidentification.getPersonname().get(0);
							if(personname != null) 
								recipientUtill.setName(personname.getGivenname()+" "+personname.getLastname());
						}
						
						Contactpoint contactpoint = person.getContactpoint().get(0);
						if(contactpoint !=null) {
							if(!contactpoint.getPhoneaddress().isEmpty()) {
								for(Phoneaddress phoneaddress : contactpoint.getPhoneaddress()) {
										if(phoneaddress.getPhoneaddresstype().trim().equalsIgnoreCase("Business Telephone") || phoneaddress.getPhoneaddresstype().trim().equalsIgnoreCase("Home")) {
											recipientUtill.setPhone(phoneaddress.getPhonenumber());
											break;
										}else {
											recipientUtill.setPhone(phoneaddress.getPhonenumber2());
										}
								}
								for(Phoneaddress phoneaddress : contactpoint.getPhoneaddress()) {

										if(phoneaddress.getPhoneaddresstype().trim().equalsIgnoreCase("Fax")) {
											recipientUtill.setFax(phoneaddress.getPhonenumber());
											break;
										}else {
											recipientUtill.setFax(phoneaddress.getFaxnumber());
										}
								}
								
							}
							Electronicaddress electronicaddress= contactpoint.getElectronicaddress().get(0);
							if(electronicaddress != null) {
								recipientUtill.setEmail(electronicaddress.getEmailaddress());
							}
							
							if(!contactpoint.getPostaladdress().isEmpty()) {
								for(Postaladdress postaladdress: contactpoint.getPostaladdress()) {
									RecipientUtill recipient = new RecipientUtill();
									recipient.setChannelCode(recipientUtill.getChannelCode());
									recipient.setCopyType(recipientUtill.getCopyType());
									recipient.setCountry(recipientUtill.getCountry());
									recipient.setEmail(recipientUtill.getEmail());
									recipient.setFax(recipientUtill.getFax());
									recipient.setId(recipientUtill.getId());
									recipient.setName(recipientUtill.getName());
									recipient.setPhone(recipientUtill.getPhone());
									recipient.setReportingLanguage(recipientUtill.getReportingLanguage());
									
									recipient.setAddressline1(postaladdress.getAddresslineone());
									recipient.setAddressline2(postaladdress.getAddresslinetwo());
									recipient.setAddressline3(postaladdress.getAddresslinethree());
									recipient.setAddressline4(postaladdress.getAddresslinefour());
									recipient.setAddressline5(postaladdress.getAddresslinefive());
									recipient.setAddressline6(postaladdress.getAddresslinesix());
									recipient.setAddressline7(postaladdress.getAddresslineseven());
									recipient.setCountry(postaladdress.getCountry().getName());
									
									if(postaladdress.getGenericidentification().getExpirydate() == null) {
										recipient.setCopyType("Investor New");
										recipientUtills.add(recipient);
									}else if(postaladdress.getGenericidentification().getExpirydate().contains("399")) {
										recipient.setCopyType("Investor New");
										recipientUtills.add(recipient);
									}else {
										recipient.setExpirydate(stringToDate(postaladdress.getGenericidentification().getExpirydate()));
										temprecipientUtills.add(recipient);
									}
								}
							}
							
						}	
						}
					
					//documentType.setInvestor(inverstorAggriate);
				}
				
				else if(inverstorAggriate.getPartytype().equalsIgnoreCase("Organization or Corporate") ||  inverstorAggriate.getPartytype().equalsIgnoreCase("Corporate")) {

					Organisation organisation = inverstorAggriate.getOrganisation().get(0);
					if(organisation != null) {
						Organisationidentification organisationidentification = organisation.getOrganisationidentification().get(0);
						RecipientUtill recipientUtill = new RecipientUtill();
						recipientUtill.setReportingLanguage("en");
						recipientUtill.setChannelCode("PRINT");
						recipientUtill.setCopyType("Investor Old");
						if(organisationidentification != null) {
							Partyidentificationinformation partyidentificationinformation = organisationidentification.getPartyidentificationinformation();
							if(partyidentificationinformation != null) 
								recipientUtill.setId(partyidentificationinformation.getGenericidentification().getIdentification());
							
							Organisationname organisationname = organisationidentification.getOrganisationname();
							if(organisationname != null) 
								recipientUtill.setName(organisationname.getPartyname().getName());
						}
						
						Contactpoint contactpoint = organisation.getContactpoint().get(0);
						if(contactpoint !=null) {
							if(!contactpoint.getPhoneaddress().isEmpty()) {
								for(Phoneaddress phoneaddress : contactpoint.getPhoneaddress()) {
										if(phoneaddress.getPhoneaddresstype().trim().equalsIgnoreCase("Business Telephone") || phoneaddress.getPhoneaddresstype().trim().equalsIgnoreCase("Home")) {
											recipientUtill.setPhone(phoneaddress.getPhonenumber());
											break;
										}else {
											recipientUtill.setPhone(phoneaddress.getPhonenumber2());
										}
								}
								for(Phoneaddress phoneaddress : contactpoint.getPhoneaddress()) {
										if(phoneaddress.getPhoneaddresstype().trim().equalsIgnoreCase("Fax")) {
											recipientUtill.setFax(phoneaddress.getPhonenumber());
											break;
										}else {
											recipientUtill.setFax(phoneaddress.getFaxnumber());
										}
								}
								
							}
							Electronicaddress electronicaddress= contactpoint.getElectronicaddress().get(0);
							if(electronicaddress != null) {
								recipientUtill.setEmail(electronicaddress.getEmailaddress());
							}
							
							if(!contactpoint.getPostaladdress().isEmpty()) {
								for(Postaladdress postaladdress: contactpoint.getPostaladdress()) {
									RecipientUtill recipient = new RecipientUtill();
									recipient.setChannelCode(recipientUtill.getChannelCode());
									recipient.setCopyType(recipientUtill.getCopyType());
									recipient.setCountry(recipientUtill.getCountry());
									recipient.setEmail(recipientUtill.getEmail());
									recipient.setFax(recipientUtill.getFax());
									recipient.setId(recipientUtill.getId());
									recipient.setName(recipientUtill.getName());
									recipient.setPhone(recipientUtill.getPhone());
									recipient.setReportingLanguage(recipientUtill.getReportingLanguage());
									
									recipient.setAddressline1(postaladdress.getAddresslineone());
									recipient.setAddressline2(postaladdress.getAddresslinetwo());
									recipient.setAddressline3(postaladdress.getAddresslinethree());
									recipient.setAddressline4(postaladdress.getAddresslinefour());
									recipient.setAddressline5(postaladdress.getAddresslinefive());
									recipient.setAddressline6(postaladdress.getAddresslinesix());
									recipient.setAddressline7(postaladdress.getAddresslineseven());
									recipient.setCountry(postaladdress.getCountry().getName());
									
									if(postaladdress.getGenericidentification().getExpirydate() == null) {
										recipient.setCopyType("Investor New");
										recipientUtills.add(recipient);
									}else if(postaladdress.getGenericidentification().getExpirydate().contains("399")) {
										recipient.setCopyType("Investor New");
										recipientUtills.add(recipient);
									}else {
										recipient.setExpirydate(stringToDate(postaladdress.getGenericidentification().getExpirydate()));
										temprecipientUtills.add(recipient);
									}
								}
							}
							
						}	
						}
					
					//documentType.setInvestor(inverstorAggriate);
				
				}
			}
		}
		
		Collections.sort(temprecipientUtills, new Comparator<RecipientUtill>() {

			@Override
			public int compare(RecipientUtill o1, RecipientUtill o2) {
				// TODO Auto-generated method stub
				return o2.getExpirydate().compareTo(o1.getExpirydate());
			}
		});
		
		
		recipientUtills.addAll(temprecipientUtills);
		
		Document document = new Document();
		
		for(RecipientUtill recipientUtill : recipientUtills) {
			System.out.println(recipientUtill);
			Recipients recipients = new Recipients();
			Recipient recipient = new Recipient();
			recipient.setChannelCode(recipientUtill.getChannelCode());
			recipient.setCopyType(recipientUtill.getCopyType());
			recipient.setCountry(recipientUtill.getCountry());
			recipient.setEmail(recipientUtill.getEmail());
			recipient.setFax(Integer.parseInt(recipientUtill.getFax()));
			recipient.setId(Integer.parseInt(recipientUtill.getId()));
			recipient.setName(recipientUtill.getName());
			recipient.setPhone(Integer.parseInt(recipientUtill.getPhone()));
			recipient.setReportingLanguage(recipientUtill.getReportingLanguage());
			
			Address address = new Address();
			address.getLine().add(recipientUtill.getAddressline1());
			address.getLine().add(recipientUtill.getAddressline2());
			address.getLine().add(recipientUtill.getAddressline3());
			address.getLine().add(recipientUtill.getAddressline4());
			address.getLine().add(recipientUtill.getAddressline5());
			address.getLine().add(recipientUtill.getAddressline6());
			address.getLine().add(recipientUtill.getAddressline7());
			recipient.setAddress(address);
			recipients.setRecipient(recipient);
			document.getRecipients().add(recipients);
		}
		
		
	}
	
	public static Date stringToDate(String date) {
		if(date != null) {
		Date date1=null;
		try {
			date1=new SimpleDateFormat("yyyy-MM-dd").parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date1;
		}
		return null;
	}
	public static String dateToString(Date date){
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		formatter.format(date);
		
		String str = formatter.format(date);
		return str;
	}
}
