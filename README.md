ConnectHub
ConnectHub is a comprehensive social networking platform that allows users to build connections, share content, and engage in interactive communication. The platform is designed with scalability and user-centric features in mind, ensuring a smooth and enriching social media experience.
1.	User
o	Represents individual users of the platform.
o	Attributes:
	userID: Unique identifier for the user.
	name, email, password, dateOfBirth: Basic information for user identification and authentication.
	createdDate, updatedDate: Timestamps for tracking the user's account lifecycle.
	status: Indicates the user's activity status (active, banned, etc.).
2.	Profile
o	Contains information related to the user's personal profile.
o	Attributes:
	profileID: Unique identifier for the profile.
	bio, profilePicture, coverPhoto: Details to display on the user's profile page.
	hometown, relationshipStatus: Additional personal details.
	updatedTimestamp: Tracks the last update time of the profile.
o	Relationships:
	Linked to a single User.
3.	Post
o	Represents posts made by users on their profiles or in groups.
o	Attributes:
	postID: Unique identifier for the post.
	content: Text content of the post.
	mediaURL: Links to attached media files like images or videos.
	createdAt, updatedAt: Timestamps for tracking post lifecycle.
	privacy: Controls visibility (e.g., public, friends-only).
o	Relationships:
	Linked to a User (author) or a Group.
4.	Comment
o	Represents user comments on posts.
o	Attributes:
	commentID: Unique identifier for the comment.
	content: Text of the comment.
	createdAt: Timestamp for when the comment was made.
o	Relationships:
	Belongs to a single Post.
	Authored by a single User.
5.	Like
o	Captures likes or reactions on posts or comments.
o	Attributes:
	likeID: Unique identifier for the like/reaction.
	reactionType: Indicates the type of reaction (e.g., like, love, angry).
	createdAt: Timestamp for when the reaction was made.
o	Relationships:
	Linked to a single Post or Comment.
	Performed by a single User.
6.	Friend
o	Represents friendships between users.
o	Attributes:
	friendshipID: Unique identifier for the friendship.
	status: Current state of the friendship (e.g., pending, accepted, blocked).
	createdAt: Timestamp for when the friendship was created.
o	Relationships:
	Connects two User accounts.
7.	Message
o	Stores private messages between users.
o	Attributes:
	messageID: Unique identifier for the message.
	content: Text of the message.
	timestamp: When the message was sent.
	readStatus: Indicates if the message has been read.
o	Relationships:
	Sent between two User accounts.
8.	Chat
o	Represents a collection of messages in a private chat or group conversation.
o	Attributes:
	chatID: Unique identifier for the chat.
	chatType: Type of chat (e.g., private, group).
o	Relationships:
	Involves multiple User accounts.
9.	Group
o	Represents community or topic-based groups users can join.
o	Attributes:
	groupID: Unique identifier for the group.
	name, description: Details about the group.
	privacyLevel: Visibility of the group (e.g., public, private).
	createdAt: Timestamp for group creation.
o	Relationships:
	Linked to Post, User accounts (as members or admins).
10.	Notification
o	Handles alerts sent to users about events on the platform.
o	Attributes:
	notificationID: Unique identifier for the notification.
	message: Content of the notification.
	isRead: Indicates if the user has read it.
	createdAt: Timestamp for when the notification was created.
o	Relationships:
	Sent to a single User.
11.	FriendRequest
o	Captures friendship requests between users.
o	Attributes:
	requestID: Unique identifier for the request.
	status: Current state of the request (e.g., pending, accepted, rejected).
	sentAt: Timestamp for when the request was sent.
o	Relationships:
	Sent between two User accounts.
12.	Verification
o	Represents account verification processes for users.
o	Attributes:
	verificationID: Unique identifier for the verification request.
	status: Indicates approval status.
	requestedAt: Timestamp for when verification was initiated.
o	Relationships:
	Linked to a single User.
________________________________________
Additional Notes
•	Primary Features:
o	Posts with likes, comments, and reactions.
o	User profiles and friend relationships.
o	Direct messages and group conversations.
•	Scalability:
o	Efficient use of identifiers (IDs) for entities like users, posts, and messages ensures future scaling.
•	Privacy:
o	Controlled by attributes in entities like posts, groups, and user accounts.
Would you like me to refine this further or add any other class-specific details?


