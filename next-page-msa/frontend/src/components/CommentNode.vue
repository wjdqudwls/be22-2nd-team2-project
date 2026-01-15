<template>
  <div class="comment-node card-subtle" style="padding: 15px; margin-bottom: 10px; border-radius: 12px;">
    <div style="display: flex; justify-content: space-between; align-items: start; margin-bottom: 8px;">
      <div>
        <span style="font-weight: 700; font-size: 0.95rem; color: var(--primary-color);">{{ comment.writerNicknm }}</span>
        <span style="font-size: 0.8rem; color: var(--text-muted); margin-left: 8px;">{{ new Date(comment.createdAt).toLocaleString() }}</span>
      </div>
      <div style="display: flex; gap: 5px;">
        <button v-if="!isEditing && canEdit" class="btn btn-ghost btn-sm" @click="startEdit" style="padding: 2px 5px; font-size: 0.8rem;">✏️</button>
        <button v-if="canEdit" class="btn btn-ghost btn-sm" @click="deleteSelf" style="padding: 2px 5px; font-size: 0.8rem; color: red;">🗑️</button>
        <button class="btn btn-ghost btn-sm" @click="toggleReply" style="padding: 2px 8px; font-size: 0.8rem;">💬 답글</button>
      </div>
    </div>

    <div v-if="!isEditing" class="comment-content" style="font-size: 1rem; color: var(--text-color); line-height: 1.5; margin-bottom: 5px;">{{ comment.content }}</div>
    <div v-else style="margin-bottom: 10px;">
      <textarea v-model="editContent" class="form-control" rows="2"></textarea>
      <div style="margin-top: 5px; text-align: right;">
        <button @click="saveEdit" class="btn btn-primary btn-sm" style="padding: 2px 10px;">저장</button>
        <button @click="cancelEdit" class="btn btn-outline btn-sm" style="padding: 2px 10px;">취소</button>
      </div>
    </div>

    <div v-if="showReplyForm" style="margin-top: 15px; align-items: flex-start; gap: 10px; animation: fadeIn 0.3s ease; display: flex;">
      <textarea v-model="replyContent" class="form-control" rows="2" style="font-size: 0.9rem; border-radius: 12px; resize: none; padding: 10px;" placeholder="답글을 남겨주세요..."></textarea>
      <button class="btn btn-primary" style="padding: 0 20px; font-size: 0.85rem; border-radius: 20px; height: 50px; white-space: nowrap;" @click="submitReply">등록</button>
    </div>

    <div v-if="comment.children && comment.children.length > 0" class="comment-children" style="margin-left: 20px; border-left: 2px solid rgba(255,255,255,0.1); padding-left: 15px; margin-top: 15px;">
      <CommentNode v-for="child in comment.children" :key="child.commentId" 
        :comment="child" :current-user-id="currentUserId" :user-role="userRole"
        @reply="$emit('reply', $event)" @edit="$emit('edit', $event)" @delete="$emit('delete', $event)"></CommentNode>
    </div>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'

// Recursive component requires self-reference or correct import
// In Vue 3 Sfc with script setup, self-referencing for recursion is automatic if name strictly matches filename usually,
// but explicit import is safer for recursive structure.
// However, since we are inside CommentNode.vue, we can just use the name `CommentNode` in template if we define it, or just use `comment-node`?
// In Vue 3 SFC, we can simply import it recursively.
// Actually, simple self-reference works. But let's export it.
</script>
<script>
export default {
  name: 'CommentNode'
}
</script>

<script setup>
const props = defineProps(['comment', 'currentUserId', 'userRole'])
const emit = defineEmits(['reply', 'edit', 'delete'])

const showReplyForm = ref(false)
const replyContent = ref('')
const isEditing = ref(false)
const editContent = ref('')

const canEdit = computed(() => {
  // Loose auth check for UI
  return (props.currentUserId && props.comment.writerId === props.currentUserId) || 
         (props.userRole === 'ADMIN' || props.userRole === 'ROLE_ADMIN')
})

const toggleReply = () => { showReplyForm.value = !showReplyForm.value }

const submitReply = () => {
    if (!replyContent.value) return
    const replyLink = props.comment._links && props.comment._links.reply ? props.comment._links.reply.href : null
    emit('reply', { 
        content: replyContent.value, 
        parentId: props.comment.commentId, 
        link: replyLink, 
        callback: () => { replyContent.value = ''; showReplyForm.value = false } 
    })
}

const startEdit = () => { editContent.value = props.comment.content; isEditing.value = true }
const cancelEdit = () => { isEditing.value = false }
const saveEdit = () => {
    emit('edit', { 
        commentId: props.comment.commentId, 
        content: editContent.value, 
        callback: () => { isEditing.value = false } 
    })
}

const deleteSelf = () => {
    emit('delete', { commentId: props.comment.commentId })
}
</script>
