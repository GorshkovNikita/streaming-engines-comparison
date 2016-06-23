package diploma.processors;

import twitter4j.Status;
import twitter4j.TwitterException;
import twitter4j.TwitterObjectFactory;

import java.io.Serializable;

/**
 * Created by Никита on 16.06.2016.
 */
public class StatusFilterProcessor implements Processor<Status, String>, Serializable {
    @Override
    public Status process(String statusJson) {
        try {
            return TwitterObjectFactory.createStatus(statusJson);
        }
        catch (TwitterException e) {
            return null;
        }
    }

    public static void main(String[] args) {
        long start = System.nanoTime();
        String str = "{\"created_at\":\"Fri Jun 17 20:02:57 +0000 2016\",\"id\":743896736872206337,\"id_str\":\"743896736872206337\",\"text\":\"RT @JMDReid: Sailing over the Skyrift is my entry for the Story Hop! Join the fun! #Fantasy #IARTG\\nhttps:\\/\\/t.co\\/oCDCIjV2KH https:\\/\\/t.co\\/AzV\\u2026\",\"source\":\"\\u003ca href=\\\"https:\\/\\/roundteam.co\\\" rel=\\\"nofollow\\\"\\u003eRoundTeam\\u003c\\/a\\u003e\",\"truncated\":false,\"in_reply_to_status_id\":null,\"in_reply_to_status_id_str\":null,\"in_reply_to_user_id\":null,\"in_reply_to_user_id_str\":null,\"in_reply_to_screen_name\":null,\"user\":{\"id\":1316962868,\"id_str\":\"1316962868\",\"name\":\"Gordon Brewer\",\"screen_name\":\"Gordon_Brewer_1\",\"location\":\"TN\",\"url\":\"http:\\/\\/www.gordonbrewer.com\",\"description\":\"#Fantasy #Author of the Clovel Sword Chronicles series of stories  http:\\/\\/www.beowulfstories.com- #ASMSG #darkfantasy #books \\nheader\\u00a9Dusan Kostic\",\"protected\":false,\"verified\":false,\"followers_count\":5223,\"friends_count\":4147,\"listed_count\":758,\"favourites_count\":152,\"statuses_count\":242265,\"created_at\":\"Sat Mar 30 18:30:36 +0000 2013\",\"utc_offset\":-18000,\"time_zone\":\"Central Time (US & Canada)\",\"geo_enabled\":false,\"lang\":\"en\",\"contributors_enabled\":false,\"is_translator\":false,\"profile_background_color\":\"6D108F\",\"profile_background_image_url\":\"http:\\/\\/pbs.twimg.com\\/profile_background_images\\/534546986154725376\\/B0Sx4AZl.jpeg\",\"profile_background_image_url_https\":\"https:\\/\\/pbs.twimg.com\\/profile_background_images\\/534546986154725376\\/B0Sx4AZl.jpeg\",\"profile_background_tile\":true,\"profile_link_color\":\"0084B4\",\"profile_sidebar_border_color\":\"FFFFFF\",\"profile_sidebar_fill_color\":\"DDEEF6\",\"profile_text_color\":\"333333\",\"profile_use_background_image\":true,\"profile_image_url\":\"http:\\/\\/pbs.twimg.com\\/profile_images\\/714211827765346305\\/_B25tJ0N_normal.jpg\",\"profile_image_url_https\":\"https:\\/\\/pbs.twimg.com\\/profile_images\\/714211827765346305\\/_B25tJ0N_normal.jpg\",\"profile_banner_url\":\"https:\\/\\/pbs.twimg.com\\/profile_banners\\/1316962868\\/1459211557\",\"default_profile\":false,\"default_profile_image\":false,\"following\":null,\"follow_request_sent\":null,\"notifications\":null},\"geo\":null,\"coordinates\":null,\"place\":null,\"contributors\":null,\"retweeted_status\":{\"created_at\":\"Fri Jun 17 19:30:05 +0000 2016\",\"id\":743888463510986753,\"id_str\":\"743888463510986753\",\"text\":\"Sailing over the Skyrift is my entry for the Story Hop! Join the fun! #Fantasy #IARTG\\nhttps:\\/\\/t.co\\/oCDCIjV2KH https:\\/\\/t.co\\/AzVjWlpCDw\",\"source\":\"\\u003ca href=\\\"https:\\/\\/about.twitter.com\\/products\\/tweetdeck\\\" rel=\\\"nofollow\\\"\\u003eTweetDeck\\u003c\\/a\\u003e\",\"truncated\":false,\"in_reply_to_status_id\":null,\"in_reply_to_status_id_str\":null,\"in_reply_to_user_id\":null,\"in_reply_to_user_id_str\":null,\"in_reply_to_screen_name\":null,\"user\":{\"id\":615843425,\"id_str\":\"615843425\",\"name\":\"JMD Reid\",\"screen_name\":\"JMDReid\",\"location\":\"Tacoma, WA\",\"url\":\"http:\\/\\/www.JMD-Reid.com\",\"description\":\"Author of the upcoming novel Above the Storm, Book 1 of the Storm Below Series. I write #fantasy and love to read.\",\"protected\":false,\"verified\":false,\"followers_count\":26342,\"friends_count\":19831,\"listed_count\":1079,\"favourites_count\":21945,\"statuses_count\":218807,\"created_at\":\"Sat Jun 23 05:47:50 +0000 2012\",\"utc_offset\":-25200,\"time_zone\":\"Pacific Time (US & Canada)\",\"geo_enabled\":false,\"lang\":\"en\",\"contributors_enabled\":false,\"is_translator\":false,\"profile_background_color\":\"C0DEED\",\"profile_background_image_url\":\"http:\\/\\/abs.twimg.com\\/images\\/themes\\/theme1\\/bg.png\",\"profile_background_image_url_https\":\"https:\\/\\/abs.twimg.com\\/images\\/themes\\/theme1\\/bg.png\",\"profile_background_tile\":false,\"profile_link_color\":\"0084B4\",\"profile_sidebar_border_color\":\"C0DEED\",\"profile_sidebar_fill_color\":\"DDEEF6\",\"profile_text_color\":\"333333\",\"profile_use_background_image\":true,\"profile_image_url\":\"http:\\/\\/pbs.twimg.com\\/profile_images\\/488186323446927360\\/G8DwmOUU_normal.jpeg\",\"profile_image_url_https\":\"https:\\/\\/pbs.twimg.com\\/profile_images\\/488186323446927360\\/G8DwmOUU_normal.jpeg\",\"profile_banner_url\":\"https:\\/\\/pbs.twimg.com\\/profile_banners\\/615843425\\/1415465534\",\"default_profile\":true,\"default_profile_image\":false,\"following\":null,\"follow_request_sent\":null,\"notifications\":null},\"geo\":null,\"coordinates\":null,\"place\":null,\"contributors\":null,\"is_quote_status\":false,\"retweet_count\":4,\"favorite_count\":0,\"entities\":{\"hashtags\":[{\"text\":\"Fantasy\",\"indices\":[70,78]},{\"text\":\"IARTG\",\"indices\":[79,85]}],\"urls\":[{\"url\":\"https:\\/\\/t.co\\/oCDCIjV2KH\",\"expanded_url\":\"http:\\/\\/bit.ly\\/1PYo2S9\",\"display_url\":\"bit.ly\\/1PYo2S9\",\"indices\":[86,109]}],\"user_mentions\":[],\"symbols\":[],\"media\":[{\"id\":718416750472069122,\"id_str\":\"718416750472069122\",\"indices\":[110,133],\"media_url\":\"http:\\/\\/pbs.twimg.com\\/media\\/CfhUOmyUAAIpLv_.jpg\",\"media_url_https\":\"https:\\/\\/pbs.twimg.com\\/media\\/CfhUOmyUAAIpLv_.jpg\",\"url\":\"https:\\/\\/t.co\\/AzVjWlpCDw\",\"display_url\":\"pic.twitter.com\\/AzVjWlpCDw\",\"expanded_url\":\"http:\\/\\/twitter.com\\/JMDReid\\/status\\/718417083432722433\\/photo\\/1\",\"type\":\"photo\",\"sizes\":{\"medium\":{\"w\":600,\"h\":549,\"resize\":\"fit\"},\"small\":{\"w\":340,\"h\":311,\"resize\":\"fit\"},\"large\":{\"w\":1024,\"h\":938,\"resize\":\"fit\"},\"thumb\":{\"w\":150,\"h\":150,\"resize\":\"crop\"}},\"source_status_id\":718417083432722433,\"source_status_id_str\":\"718417083432722433\",\"source_user_id\":615843425,\"source_user_id_str\":\"615843425\"}]},\"extended_entities\":{\"media\":[{\"id\":718416750472069122,\"id_str\":\"718416750472069122\",\"indices\":[110,133],\"media_url\":\"http:\\/\\/pbs.twimg.com\\/media\\/CfhUOmyUAAIpLv_.jpg\",\"media_url_https\":\"https:\\/\\/pbs.twimg.com\\/media\\/CfhUOmyUAAIpLv_.jpg\",\"url\":\"https:\\/\\/t.co\\/AzVjWlpCDw\",\"display_url\":\"pic.twitter.com\\/AzVjWlpCDw\",\"expanded_url\":\"http:\\/\\/twitter.com\\/JMDReid\\/status\\/718417083432722433\\/photo\\/1\",\"type\":\"photo\",\"sizes\":{\"medium\":{\"w\":600,\"h\":549,\"resize\":\"fit\"},\"small\":{\"w\":340,\"h\":311,\"resize\":\"fit\"},\"large\":{\"w\":1024,\"h\":938,\"resize\":\"fit\"},\"thumb\":{\"w\":150,\"h\":150,\"resize\":\"crop\"}},\"source_status_id\":718417083432722433,\"source_status_id_str\":\"718417083432722433\",\"source_user_id\":615843425,\"source_user_id_str\":\"615843425\"}]},\"favorited\":false,\"retweeted\":false,\"possibly_sensitive\":false,\"filter_level\":\"low\",\"lang\":\"en\"},\"is_quote_status\":false,\"retweet_count\":0,\"favorite_count\":0,\"entities\":{\"hashtags\":[{\"text\":\"Fantasy\",\"indices\":[83,91]},{\"text\":\"IARTG\",\"indices\":[92,98]}],\"urls\":[{\"url\":\"https:\\/\\/t.co\\/oCDCIjV2KH\",\"expanded_url\":\"http:\\/\\/bit.ly\\/1PYo2S9\",\"display_url\":\"bit.ly\\/1PYo2S9\",\"indices\":[99,122]}],\"user_mentions\":[{\"screen_name\":\"JMDReid\",\"name\":\"JMD Reid\",\"id\":615843425,\"id_str\":\"615843425\",\"indices\":[3,11]}],\"symbols\":[],\"media\":[{\"id\":718416750472069122,\"id_str\":\"718416750472069122\",\"indices\":[123,140],\"media_url\":\"http:\\/\\/pbs.twimg.com\\/media\\/CfhUOmyUAAIpLv_.jpg\",\"media_url_https\":\"https:\\/\\/pbs.twimg.com\\/media\\/CfhUOmyUAAIpLv_.jpg\",\"url\":\"https:\\/\\/t.co\\/AzVjWlpCDw\",\"display_url\":\"pic.twitter.com\\/AzVjWlpCDw\",\"expanded_url\":\"http:\\/\\/twitter.com\\/JMDReid\\/status\\/718417083432722433\\/photo\\/1\",\"type\":\"photo\",\"sizes\":{\"medium\":{\"w\":600,\"h\":549,\"resize\":\"fit\"},\"small\":{\"w\":340,\"h\":311,\"resize\":\"fit\"},\"large\":{\"w\":1024,\"h\":938,\"resize\":\"fit\"},\"thumb\":{\"w\":150,\"h\":150,\"resize\":\"crop\"}},\"source_status_id\":718417083432722433,\"source_status_id_str\":\"718417083432722433\",\"source_user_id\":615843425,\"source_user_id_str\":\"615843425\"}]},\"extended_entities\":{\"media\":[{\"id\":718416750472069122,\"id_str\":\"718416750472069122\",\"indices\":[123,140],\"media_url\":\"http:\\/\\/pbs.twimg.com\\/media\\/CfhUOmyUAAIpLv_.jpg\",\"media_url_https\":\"https:\\/\\/pbs.twimg.com\\/media\\/CfhUOmyUAAIpLv_.jpg\",\"url\":\"https:\\/\\/t.co\\/AzVjWlpCDw\",\"display_url\":\"pic.twitter.com\\/AzVjWlpCDw\",\"expanded_url\":\"http:\\/\\/twitter.com\\/JMDReid\\/status\\/718417083432722433\\/photo\\/1\",\"type\":\"photo\",\"sizes\":{\"medium\":{\"w\":600,\"h\":549,\"resize\":\"fit\"},\"small\":{\"w\":340,\"h\":311,\"resize\":\"fit\"},\"large\":{\"w\":1024,\"h\":938,\"resize\":\"fit\"},\"thumb\":{\"w\":150,\"h\":150,\"resize\":\"crop\"}},\"source_status_id\":718417083432722433,\"source_status_id_str\":\"718417083432722433\",\"source_user_id\":615843425,\"source_user_id_str\":\"615843425\"}]},\"favorited\":false,\"retweeted\":false,\"possibly_sensitive\":false,\"filter_level\":\"low\",\"lang\":\"en\",\"timestamp_ms\":\"1466193777660\"}";
        Status res = new StatusFilterProcessor().process(str);
        System.out.println(res.getText());
        System.out.println("\n" + (System.nanoTime() - start) / 1000000);
    }
}
