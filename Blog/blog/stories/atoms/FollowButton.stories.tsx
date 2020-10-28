import React from 'react';

import { FollowButton, FollowButtonProps } from 'components/atoms/FollowButton';

export default {
  component: FollowButton,
  title: 'Atoms/Follow Button',
};

const Template = (args: FollowButtonProps) => <FollowButton {...args} />;

export const Default = Template.bind({});
// eslint-disable-next-line @typescript-eslint/ban-ts-comment
// @ts-ignore-next-line
Default.args = {
  toFollow: {
    id: '1234',
    name: 'Test User',
    following: [],
  },
};

export const UserFollowing = Template.bind({});
// eslint-disable-next-line @typescript-eslint/ban-ts-comment
// @ts-ignore-next-line
UserFollowing.args = {
  toFollow: {
    id: '4567',
    name: 'Test User',
    following: [],
  },
};
