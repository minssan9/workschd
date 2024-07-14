<template>
  <div>
    <Menubar :model="items" />

    <Toolbar>
      <template #start>
        <Button icon="pi pi-plus" class="mr-2" severity="secondary" text />
        <Button icon="pi pi-print" class="mr-2" severity="secondary" text />
        <Button icon="pi pi-upload" severity="secondary" text />
      </template>

      <template #center>
        <IconField>
          <InputIcon>
            <i class="pi pi-search" />
          </InputIcon>
          <InputText placeholder="Search" />
        </IconField>
      </template>

      <template #end> <SplitButton label="Save" :model="items"></SplitButton></template>
    </Toolbar>
    <slot></slot>
  </div>
</template>

<script setup>
  import { ref } from "vue";

  const items = ref([
    {
      label: 'Home',
      icon: 'pi pi-home'
    },
    {
      label: 'Features',
      icon: 'pi pi-star'
    },
    {
      label: 'Projects',
      icon: 'pi pi-search',
      items: [
        {
          label: 'Components',
          icon: 'pi pi-bolt'
        },
        {
          label: 'Blocks',
          icon: 'pi pi-server'
        },
        {
          label: 'UI Kit',
          icon: 'pi pi-pencil'
        },
        {
          label: 'Templates',
          icon: 'pi pi-palette',
          items: [
            {
              label: 'Apollo',
              icon: 'pi pi-palette'
            },
            {
              label: 'Ultima',
              icon: 'pi pi-palette'
            }
          ]
        }
      ]
    },
    {
      label: 'Contact',
      icon: 'pi pi-envelope'
    }
  ]);
</script>