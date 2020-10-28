import React from 'react';

import { PostCard, PostCardProps } from 'components/molecules/PostCard';

import { DEFAULT_POST } from 'defaults/Post';

export default {
  component: PostCard,
  title: 'Molecules/Post Card',
};

const Template = (args: PostCardProps) => <PostCard {...args} />;

export const Default = Template.bind({});
// eslint-disable-next-line @typescript-eslint/ban-ts-comment
// @ts-ignore-next-line
Default.args = {
  post: DEFAULT_POST,
};
