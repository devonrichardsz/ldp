import {
  Card,
  CardHeader,
  CardActions,
  CardContent,
  CardMedia,
  Typography,
} from '@material-ui/core';
import { makeStyles } from '@material-ui/core/styles';
import useTimeAgo from '@rooks/use-time-ago';
import React from 'react';

import { Likes } from 'components/atoms/Likes';
import { UserAvatar } from 'components/atoms/UserAvatar';
import { useUserById } from 'services/UserService';
import { IPost } from 'types/Post';

const useStyles = makeStyles({
  root: {
    maxWidth: 256,
  },
});

export interface PostCardProps {
  post: IPost;
}

export const PostCard: React.FC<PostCardProps> = ({ post }: PostCardProps) => {
  const author = useUserById(post.author);
  // eslint-disable-next-line @typescript-eslint/ban-ts-comment
  // @ts-ignore-next-line
  const timeAgo = useTimeAgo(post.createdAt);
  const classes = useStyles();
  return (
    <Card className={classes.root}>
      <CardHeader
        avatar={<UserAvatar user={author} />}
        title={post.title}
        subheader={author ? `${author.name} - ${timeAgo}` : timeAgo}
      />
      {post.thumbnail && <CardMedia component="img" image={post.thumbnail} title={post.title} />}
      <CardContent>
        <Typography variant="body2">{post.summary}</Typography>
      </CardContent>
      <CardActions>
        <Likes users={post.likes} />
      </CardActions>
    </Card>
  );
};

export default PostCard;
