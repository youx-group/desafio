<template>
  <Teleport to="body">
    <Transition name="modal">
      <div v-if="isOpen" class="fixed inset-0 z-50 overflow-y-auto">
        <!-- Overlay -->
        <div 
          class="fixed inset-0 bg-black bg-opacity-50 transition-opacity"
          @click="handleClose"
        />
        
        <!-- Modal -->
        <div class="flex min-h-full items-center justify-center p-4">
          <div 
            :class="[
              'relative bg-white rounded-lg shadow-xl w-full max-h-[90vh] overflow-y-auto',
              sizeClasses[size]
            ]"
            @click.stop
          >
            <!-- Header -->
            <div class="flex items-center justify-between p-6 border-b border-gray-200">
              <h2 class="text-xl font-semibold text-gray-900">{{ title }}</h2>
              <button
                @click="handleClose"
                class="p-2 hover:bg-gray-100 rounded-full transition-colors"
              >
                <X class="w-5 h-5 text-gray-500" />
              </button>
            </div>
            
            <!-- Content -->
            <div class="p-6">
              <slot />
            </div>
          </div>
        </div>
      </div>
    </Transition>
  </Teleport>
</template>

<script setup lang="ts">
import { computed, onMounted, onUnmounted } from 'vue';
import { X } from 'lucide-vue-next';

interface Props {
  isOpen: boolean;
  title: string;
  size?: 'sm' | 'md' | 'lg' | 'xl';
}

interface Emits {
  (e: 'close'): void;
}

const props = withDefaults(defineProps<Props>(), {
  size: 'md'
});

const emit = defineEmits<Emits>();

const sizeClasses = {
  sm: 'max-w-md',
  md: 'max-w-lg',
  lg: 'max-w-2xl',
  xl: 'max-w-4xl'
};

const handleClose = () => {
  emit('close');
};

// Fecha modal com Escape
const handleEscape = (e: KeyboardEvent) => {
  if (e.key === 'Escape' && props.isOpen) {
    handleClose();
  }
};

onMounted(() => {
  document.addEventListener('keydown', handleEscape);
});

onUnmounted(() => {
  document.removeEventListener('keydown', handleEscape);
});

// Previne scroll do body quando modal está aberto
const updateBodyScroll = () => {
  if (props.isOpen) {
    document.body.style.overflow = 'hidden';
  } else {
    document.body.style.overflow = 'unset';
  }
};

// Watch para mudanças no isOpen
import { watch } from 'vue';
watch(() => props.isOpen, () => {
  updateBodyScroll();
});
</script>

<style scoped>
.modal-enter-active,
.modal-leave-active {
  transition: opacity 0.3s ease;
}

.modal-enter-from,
.modal-leave-to {
  opacity: 0;
}
</style>

