import React from 'react';

import { IComment } from 'types/Post';

export interface CommentListProps {
  comments: [IComment];
}

export const CommentList: React.FC<CommentListProps> = () => <>CommentList</>;
