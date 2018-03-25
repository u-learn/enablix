/**************************************************************
Script to create reference collections to be used during new tenant setup

Execute: use mongo utility to run the script

mongo localhost:27017/admin -u enablix_app -p <password> <path>/<script-file-name>

****************************************************************/

var roles = [
	{ 
	    "_id" : "contentAdmin", 
	    "_class" : "com.enablix.core.domain.security.authorization.Role", 
	    "identity" : "contentAdmin", 
	    "roleName" : "Administrator", 
	    "permissions" : [
	    	"VIEW_STUDIO", 
	        "VIEW_REF_DATA", 
	        "VIEW_PORTAL", 
	        "VIEW_RECENT_CONTENT", 
	        "MANAGE_CONTENT_REQUEST", 
	        "SHARE_VIA_EMAIL", 
	        "SHARE_VIA_SLACK", 
	        "MANAGE_INTEGRATIONS", 
	        "VIEW_REPORT-activity-metric-calculator", 
	        "VIEW_REPORT-content-coverage-report", 
	        "VIEW_REPORTS", 
	        "VIEW_REPORT-activity-trend-calculator", 
	        "DOCSTORE_DIRECT_ACCESS"
	    ]
	},
	{ 
	    "_id" : "portalUser", 
	    "_class" : "com.enablix.core.domain.security.authorization.Role", 
	    "identity" : "portalUser", 
	    "roleName" : "Consumer", 
	    "permissions" : [
	    	"VIEW_PORTAL", 
	        "VIEW_RECENT_CONTENT", 
	        "SUGGEST_CONTENT", 
	        "SHARE_VIA_EMAIL", 
	        "SHARE_VIA_SLACK", 
	        "DOCSTORE_DIRECT_ACCESS"
	    ]
	}];

// Switch to system database
db = db.getSiblingDB("system_enablix");

var coll = db.getCollection("ref_ebx_role");

//clear the collection
coll.remove({});
coll.insert(roles);


var date = new Date();
var embedInfo = [
	{ 
	    "_class" : "com.enablix.core.domain.uri.embed.EmbedInfo", 
	    "type" : "video", 
	    "url" : "https://www.youtube.com/watch?v=j_a4daOZhyc", 
	    "site" : "YouTube", 
	    "title" : "9 Steps for Sales Enablement Success", 
	    "description" : "On today’s SBI Insider: Sales and Marketing Insights Video Podcast, we discuss how to develop and execute your sales enablement strategy. We will explain the...", 
	    "favicon" : {
	        "url" : "https://s.ytimg.com/yts/img/favicon-vfl8qSV2F.ico", 
	        "safe" : "https%3A%2F%2Fs.ytimg.com%2Fyts%2Fimg%2Ffavicon-vfl8qSV2F.ico"
	    }, 
	    "images" : [
	        {
	            "url" : "https://i.ytimg.com/vi/j_a4daOZhyc/maxresdefault.jpg", 
	            "safe" : "https%3A%2F%2Fi.ytimg.com%2Fvi%2Fj_a4daOZhyc%2Fmaxresdefault.jpg", 
	            "width" : 1920, 
	            "height" : 1080
	        }
	    ], 
	    "videos" : [
	        {
	            "url" : "https://www.youtube.com/embed/j_a4daOZhyc", 
	            "type" : "text/html"
	        }, 
	        {
	            "url" : "http://www.youtube.com/v/j_a4daOZhyc?version=3&autohide=1", 
	            "type" : "application/x-shockwave-flash"
	        }
	    ], 
	    "audios" : [

	    ], 
	    "oembed" : {
	        "type" : "video", 
	        "version" : "1.0", 
	        "title" : "9 Steps for Sales Enablement Success", 
	        "html" : "<iframe width=\"480\" height=\"270\" src=\"https://www.youtube.com/embed/j_a4daOZhyc?feature=oembed\" frameborder=\"0\" allow=\"autoplay; encrypted-media\" allowfullscreen></iframe>", 
	        "width" : 480, 
	        "height" : 270
	    }, 
	    "iframeEmbeddable" : false, 
	    "identity" : "77f89594-674d-4154-bd11-af60572072e6", 
	    "createdAt" : date, 
	    "createdBy" : "system", 
	    "createdByName" : "System", 
	    "modifiedBy" : "system", 
	    "modifiedByName" : "System", 
	    "modifiedAt" : date
	},
	{ 
	    "_class" : "com.enablix.core.domain.uri.embed.EmbedInfo", 
	    "type" : "rich", 
	    "url" : "https://enablix.wordpress.com/2018/03/12/why-storing-sales-content-inside-crm-does-not-pay-off/", 
	    "site" : "enablix blog", 
	    "title" : "Why storing sales content inside CRM does not pay off?", 
	    "description" : "If you are like any marketer, you are always hounded by your sales colleagues for latest collateral and intelligence that they can use in the field. In today’s world of specialized sales role…", 
	    "favicon" : {
	        "url" : "https://secure.gravatar.com/blavatar/9ce505fa6d2ef38030a9791cf167428b?s=32", 
	        "safe" : "https%3A%2F%2Fsecure.gravatar.com%2Fblavatar%2F9ce505fa6d2ef38030a9791cf167428b%3Fs%3D32"
	    }, 
	    "images" : [
	        {
	            "url" : "https://enablix.files.wordpress.com/2017/11/library-1220101_640.jpg", 
	            "safe" : "https%3A%2F%2Fenablix.files.wordpress.com%2F2017%2F11%2Flibrary-1220101_640.jpg", 
	            "width" : 640, 
	            "height" : 426
	        }, 
	        {
	            "url" : "https://enablix.files.wordpress.com/2017/11/library-1220101_640.jpg?w=640", 
	            "safe" : "https%3A%2F%2Fenablix.files.wordpress.com%2F2017%2F11%2Flibrary-1220101_640.jpg%3Fw%3D640", 
	            "width" : 640, 
	            "height" : 426
	        }
	    ], 
	    "videos" : [

	    ], 
	    "audios" : [

	    ], 
	    "oembed" : {
	        "type" : "link", 
	        "version" : "1.0", 
	        "title" : "Why storing sales content inside CRM does not pay&nbsp;off?", 
	        "html" : "<p>If you are like any marketer, you are always hounded by your sales colleagues for latest collateral and intelligence that they can use in the field. In today&#8217;s world of specialized sales roles of SDRs (Sales Development Reps), AEs (Account Executives), Inside Sales and event Customer Success reps, everyone is looking for information and content to help them achieve their sales goals. Therefore, having one single place where these reps can get all their content and intelligence is essential and a no-brainer. It is also true that for any decently sized sales organization, they are already using a CRM (Customer Relationship Management) system. And today&#8217;s CRM systems offer options to manage your sales collateral library in them. Therefore there is always a strong case for having the sales content stored in the CRM content library. Plus,</p>\n<ul>\n<li>Salespeople are already spending their time in CRM.</li>\n<li>Your company is already paying for the CRM system. And most CRM systems are not going to charge extra for storing the content. So it makes economic sense.</li>\n</ul>\n<p>However, when we speak with marketers, in almost every instance, we find this strategy does not scale. If you step back and look at the entire end-to-end sales enablement process, <span style=\"text-decoration:underline;\"><strong>storing</strong></span> the content inside CRM system is bound to create more work for marketers and not pay the expected dividends.</p>\n<p>Here are some challenges that you should consider before you go down this avenue.</p>\n<p><strong>You end up creating information silos. </strong></p>\n<p>If your company&#8217;s CRM usage is like the majority of other companies out there, a specific segment of your employees has access to your CRM system. It is usually limited to salespeople, marketing, and a select few executives. A very valid reason for this limited access is the cost of the CRM system.</p>\n<p>This directly results in silos of information pockets. Your product team starts using a wiki to store their content. Your sales engineering team starts using a cloud storage system to store pre-sales content. Your architects and subject matter experts start creating their own folders on your cloud storage platform or, even worse, on their desktops. Soon, you end up with content and intelligence distributed across different systems and platforms. And this naturally adds a barrier to get all relevant content centralized in the CRM application.</p>\n<p><strong>You are missing out on a lot of content. </strong></p>\n<p>When we think of content, we usually think of slide decks, white papers, and case studies. But there is so much more to content than just powerpoint presentations and pdf files. According to a <a href=\"https://t.co/yYBfxkHaR6\">recently published article</a> by Tamara Schneck, only 40% of the content comes from the marketing team.</p>\n<div data-shortcode=\"caption\" id=\"attachment_731\" style=\"width: 810px\" class=\"wp-caption alignnone\"><img data-attachment-id=\"731\" data-permalink=\"https://enablix.wordpress.com/2018/03/12/why-storing-sales-content-inside-crm-does-not-pay-off/screenshot_1893/\" data-orig-file=\"https://enablix.files.wordpress.com/2018/03/screenshot_1893.png?w=800&#038;h=242\" data-orig-size=\"800,242\" data-comments-opened=\"0\" data-image-meta=\"{&quot;aperture&quot;:&quot;0&quot;,&quot;credit&quot;:&quot;&quot;,&quot;camera&quot;:&quot;&quot;,&quot;caption&quot;:&quot;&quot;,&quot;created_timestamp&quot;:&quot;0&quot;,&quot;copyright&quot;:&quot;&quot;,&quot;focal_length&quot;:&quot;0&quot;,&quot;iso&quot;:&quot;0&quot;,&quot;shutter_speed&quot;:&quot;0&quot;,&quot;title&quot;:&quot;&quot;,&quot;orientation&quot;:&quot;0&quot;}\" data-image-title=\"screenshot_1893\" data-image-description=\"\" data-medium-file=\"https://enablix.files.wordpress.com/2018/03/screenshot_1893.png?w=800&#038;h=242?w=300\" data-large-file=\"https://enablix.files.wordpress.com/2018/03/screenshot_1893.png?w=800&#038;h=242?w=800\" class=\"alignnone size-full wp-image-731\" src=\"https://enablix.files.wordpress.com/2018/03/screenshot_1893.png?w=800&#038;h=242\" alt=\"screenshot_1893\" width=\"800\" height=\"242\" srcset=\"https://enablix.files.wordpress.com/2018/03/screenshot_1893.png 800w, https://enablix.files.wordpress.com/2018/03/screenshot_1893.png?w=150&amp;h=45 150w, https://enablix.files.wordpress.com/2018/03/screenshot_1893.png?w=300&amp;h=91 300w, https://enablix.files.wordpress.com/2018/03/screenshot_1893.png?w=768&amp;h=232 768w\" sizes=\"(max-width: 800px) 100vw, 800px\" /><p class=\"wp-caption-text\">Less than 40% of the content comes from marketing &#8211; From Tamara Schneck</p></div>\n<p>By storing content inside a CRM application, you make it difficult to capture those different nuggets of intelligence which are not in powerpoint files or case studies.</p>\n<p><strong>Sales reps are overwhelmed. </strong></p>\n<p>Here is a message from one of my ex-colleague who is an AE in a growing startup. &#8220;All of our content is in Salesforce. My biggest complaint is there is so much content that it makes it hard for me to know what content to use when and what to send to prospects.&#8221; Sales reps are after all humans. And they are humans with relatively low attention span. They are not academic and research oriented. Very soon all your content that makes its way into CRM system is too much to handle.</p>\n<p><strong>CRM systems are feature poor when it comes to content management. </strong></p>\n<p>CRM systems are specialized in helping with deal flow. They are great at many things to help sales execute. However, they are not built with necessary enablement features that help salespeople succeed.</p>\n<ul>\n<li>Poor search</li>\n<li>Poor organization capabilities</li>\n<li>No quality control</li>\n<li>No workflow</li>\n<li>And most importantly there is no analytics to see what is working and what isn&#8217;t.</li>\n</ul>\n<p><strong>It is resource intensive. </strong></p>\n<p>All the above challenges can be overcome. How? By brute force. If marketing or sales enablement managers can,</p>\n<ul>\n<li>Ensure all information from different silos makes its way into the CRM application</li>\n<li>Keep content up to date.</li>\n<li>Communicate with sales reps when new content is available.</li>\n<li>Ensure quality control on content</li>\n</ul>\n<p>However, these tasks are quite resource-intensive. You are opening avenues for low productivity and inefficiencies to creep into your sales enablement efforts.  And marketers don&#8217;t have spare time. Their time is well spent in generating leads and creating top-notch content. Not in organizing and distributing content.</p>\n<p>Several organizations do attempt to drive enablement by storing content inside their CRM systems. It may initially make sense for all the right reasons. However, it will hurt your efficiency and will not deliver the desired outcome. We have seen this approach not scale.</p>\n"
	    }, 
	    "iframeEmbeddable" : true, 
	    "identity" : "eee1257c-edd2-404b-abf4-65f74dbf14a8", 
	    "createdAt" : date, 
	    "createdBy" : "system", 
	    "createdByName" : "System", 
	    "modifiedBy" : "system", 
	    "modifiedByName" : "System", 
	    "modifiedAt" : date
	},
	{ 
	    "_class" : "com.enablix.core.domain.uri.embed.EmbedInfo", 
	    "type" : "rich", 
	    "url" : "https://www.linkedin.com/pulse/why-content-sales-enablement-covers-much-more-tamara-schenk/?utm_campaign=Twitter&utm_content=61411833&utm_medium=social&utm_source=twitter", 
	    "site" : "www.linkedin.com", 
	    "title" : "Why Content in Sales Enablement Covers Much More Than\"Marketing Content\"", 
	    "description" : "Enablement is a very fast-growing discipline: In 2012, 19% of our study participants at CSO Insights reported having a sales enablement program,", 
	    "favicon" : {
	        "url" : "https://static.licdn.com/scds/common/u/images/logos/favicons/v1/favicon.ico", 
	        "safe" : "https%3A%2F%2Fstatic.licdn.com%2Fscds%2Fcommon%2Fu%2Fimages%2Flogos%2Ffavicons%2Fv1%2Ffavicon.ico"
	    }, 
	    "images" : [
	        {
	            "url" : "https://media.licdn.com/mpr/mpr/shrinknp_400_400/gcrc/dms/image/C4E12AQFsEaFdNA7T6w/article-cover_image-shrink_720_1280/0?e=2121184800&v=alpha&t=fkj8Vw09g5xIIHyFRPKxUY126Ri2TxEedEk9bI0u46c", 
	            "safe" : "https%3A%2F%2Fmedia.licdn.com%2Fmpr%2Fmpr%2Fshrinknp_400_400%2Fgcrc%2Fdms%2Fimage%2FC4E12AQFsEaFdNA7T6w%2Farticle-cover_image-shrink_720_1280%2F0%3Fe%3D2121184800%26v%3Dalpha%26t%3Dfkj8Vw09g5xIIHyFRPKxUY126Ri2TxEedEk9bI0u46c", 
	            "width" : 1280, 
	            "height" : 720
	        }
	    ], 
	    "videos" : [

	    ], 
	    "audios" : [

	    ], 
	    "iframeEmbeddable" : false, 
	    "identity" : "771932ad-e760-434c-a18b-ae6c83f51913", 
	    "createdAt" : date, 
	    "createdBy" : "system", 
	    "createdByName" : "System", 
	    "modifiedBy" : "system", 
	    "modifiedByName" : "System", 
	    "modifiedAt" : date
	},
	{ 
	    "_class" : "com.enablix.core.domain.uri.embed.EmbedInfo", 
	    "type" : "video", 
	    "url" : "https://enablix.wistia.com/medias/5ti02nh9qq", 
	    "site" : "enablix.wistia.com", 
	    "title" : "Enablix-0dEEc36O-3Yc_1080", 
	    "description" : "1 min 17 sec video", 
	    "favicon" : {
	        "url" : "https://enablix.wistia.com/favicon.ico", 
	        "safe" : "https%3A%2F%2Fenablix.wistia.com%2Ffavicon.ico"
	    }, 
	    "images" : [
	        {
	            "url" : "http://embed.wistia.com/deliveries/d8c02aa1dc3a63f0b6df59e870eed9a01b72cfe7.jpg", 
	            "safe" : "http%3A%2F%2Fembed.wistia.com%2Fdeliveries%2Fd8c02aa1dc3a63f0b6df59e870eed9a01b72cfe7.jpg", 
	            "width" : 1920, 
	            "height" : 1080
	        }, 
	        {
	            "url" : "https://embed-ssl.wistia.com/deliveries/d8c02aa1dc3a63f0b6df59e870eed9a01b72cfe7.jpg", 
	            "safe" : "https%3A%2F%2Fembed-ssl.wistia.com%2Fdeliveries%2Fd8c02aa1dc3a63f0b6df59e870eed9a01b72cfe7.jpg", 
	            "width" : 1920, 
	            "height" : 1080
	        }, 
	        {
	            "url" : "https://embed-ssl.wistia.com/deliveries/d8c02aa1dc3a63f0b6df59e870eed9a01b72cfe7/file.jpg", 
	            "safe" : "https%3A%2F%2Fembed-ssl.wistia.com%2Fdeliveries%2Fd8c02aa1dc3a63f0b6df59e870eed9a01b72cfe7%2Ffile.jpg", 
	            "width" : 1920, 
	            "height" : 1080
	        }
	    ], 
	    "videos" : [
	        {
	            "url" : "https://fast.wistia.net/embed/iframe/5ti02nh9qq?twitter=true", 
	            "type" : "text/html"
	        }
	    ], 
	    "audios" : [

	    ], 
	    "oembed" : {
	        "type" : "video", 
	        "version" : "1.0", 
	        "title" : "Enablix-0dEEc36O-3Yc_1080", 
	        "html" : "<iframe src=\"https://fast.wistia.net/embed/iframe/5ti02nh9qq\" title=\"Wistia video player\" allowtransparency=\"true\" frameborder=\"0\" scrolling=\"no\" class=\"wistia_embed\" name=\"wistia_embed\" allowfullscreen mozallowfullscreen webkitallowfullscreen oallowfullscreen msallowfullscreen width=\"640\" height=\"360\"></iframe>\n<script src=\"https://fast.wistia.net/assets/external/E-v1.js\" async></script>", 
	        "width" : 640, 
	        "height" : 360
	    }, 
	    "iframeEmbeddable" : true, 
	    "identity" : "d15d47a6-3504-4225-9749-79c0e33df2d0", 
	    "createdAt" : date, 
	    "createdBy" : "system", 
	    "createdByName" : "System", 
	    "modifiedBy" : "system", 
	    "modifiedByName" : "System", 
	    "modifiedAt" : date
	}
];

var eIColl = db.getCollection("ref_ebx_embed_info");

//clear the collection
eIColl.remove({});
eIColl.insert(embedInfo);
