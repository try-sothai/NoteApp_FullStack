<?php

namespace Database\Seeders;

use Illuminate\Database\Seeder;
use App\Models\Category;

class CategorySeeder extends Seeder
{
    /**
     * Run the database seeds.
     */
    public function run(): void
    {
        $categories = [
            ['name' => 'Study', 'description' => 'Notes related to studies and learning.'],
            ['name' => 'Work', 'description' => 'Professional and job-related notes.'],
            ['name' => 'Personal', 'description' => 'Personal thoughts, journals, and private matters.'],
            ['name' => 'Programming', 'description' => 'Coding snippets, algorithms, and tech stack notes.'],
            ['name' => 'Technology', 'description' => 'General tech news, gadgets, and updates.'],
            ['name' => 'Business', 'description' => 'Business strategies, finances, and planning.'],
            ['name' => 'Ideas', 'description' => 'Brainstorming and innovative ideas.'],
            ['name' => 'Meeting', 'description' => 'Minutes of meetings and action items.'],
            ['name' => 'Important', 'description' => 'High-priority notes that need immediate attention.'],
            ['name' => 'Others', 'description' => 'Miscellaneous notes that don\'t fit elsewhere.'],
        ];

        foreach ($categories as $category) {
            Category::updateOrCreate(
                ['name' => $category['name']], // Search condition
                ['description' => $category['description']] // Update or insert values
            );
        }
    }
}
