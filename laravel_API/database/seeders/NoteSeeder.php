<?php

namespace Database\Seeders;

use Illuminate\Database\Seeder;
use App\Models\Note;
use App\Models\Category;

class NoteSeeder extends Seeder
{
    /**
     * Run the database seeds.
     */
    public function run(): void
    {
        $categories = Category::all()->keyBy('name');

        if ($categories->isEmpty()) {
            $this->command->warn('No categories found. Please run CategorySeeder first.');
            return;
        }

        $notesData = [
            ['category' => 'Programming', 'title' => 'Laravel REST API Development', 'content' => 'Designing robust endpoints using Laravel 11.'],
            ['category' => 'Programming', 'title' => 'Android Retrofit Integration', 'content' => 'Consuming Laravel APIs using Retrofit 2 in Java.'],
            ['category' => 'Technology', 'title' => 'MySQL Database Relationships', 'content' => 'Understanding One-to-Many and Many-to-Many.'],
            ['category' => 'Study', 'title' => 'Java Programming Practice', 'content' => 'Solving data structures and algorithm problems.'],
            ['category' => 'Study', 'title' => 'Weekly Study Plan', 'content' => 'Allocating 2 hours daily for continuous learning.'],
            ['category' => 'Work', 'title' => 'Project Development Tasks', 'content' => 'Implementing authentication and authorization.'],
            ['category' => 'Meeting', 'title' => 'Important Meeting Notes', 'content' => 'Discussed the Q3 roadmap and feature releases.'],
            ['category' => 'Business', 'title' => 'Business Planning', 'content' => 'Drafting a new monetization strategy.'],
            ['category' => 'Ideas', 'title' => 'New App Ideas', 'content' => 'Building an AI-powered note-taking app.'],
            ['category' => 'Personal', 'title' => 'Personal Daily Tasks', 'content' => 'Grocery shopping and paying utility bills.'],
            // Add 20 more notes to reach 30
            ['category' => 'Programming', 'title' => 'Docker Setup', 'content' => 'Configuring docker-compose for local development.'],
            ['category' => 'Work', 'title' => 'Client Feedback', 'content' => 'Need to adjust UI colors as requested.'],
            ['category' => 'Study', 'title' => 'Math Revision', 'content' => 'Reviewing linear algebra concepts.'],
            ['category' => 'Personal', 'title' => 'Workout Plan', 'content' => 'Monday: Chest, Tuesday: Back, Wednesday: Legs.'],
            ['category' => 'Technology', 'title' => 'AI Trends', 'content' => 'Generative AI advancements in 2026.'],
            ['category' => 'Important', 'title' => 'Passport Renewal', 'content' => 'Submit documents by next Friday.'],
            ['category' => 'Others', 'title' => 'Books to Read', 'content' => '1. Clean Code. 2. Pragmatic Programmer.'],
            ['category' => 'Ideas', 'title' => 'Blog Post Draft', 'content' => 'How to learn programming effectively in 2026.'],
            ['category' => 'Meeting', 'title' => 'Sync with Design Team', 'content' => 'Finalized the mockups for the dashboard.'],
            ['category' => 'Business', 'title' => 'Marketing Campaign', 'content' => 'Launching the new social media ads next week.'],
            ['category' => 'Programming', 'title' => 'React Hooks', 'content' => 'Deep dive into useEffect and useMemo.'],
            ['category' => 'Technology', 'title' => 'Cloud Migration', 'content' => 'Moving legacy servers to AWS EC2.'],
            ['category' => 'Study', 'title' => 'Spanish Vocabulary', 'content' => 'Memorizing 50 new words today.'],
            ['category' => 'Personal', 'title' => 'Weekend Trip', 'content' => 'Booking hotel for the upcoming holiday.'],
            ['category' => 'Work', 'title' => 'Quarterly Review', 'content' => 'Preparing slides for the performance review.'],
            ['category' => 'Meeting', 'title' => 'Investor Pitch', 'content' => 'Feedback from the seed round pitch.'],
            ['category' => 'Ideas', 'title' => 'Smart Home Setup', 'content' => 'Automating lights with IoT sensors.'],
            ['category' => 'Business', 'title' => 'Competitor Analysis', 'content' => 'Reviewing pricing models of competitors.'],
            ['category' => 'Important', 'title' => 'Tax Filing', 'content' => 'Gathering documents for the accountant.'],
            ['category' => 'Others', 'title' => 'Movies to Watch', 'content' => 'Inception, Interstellar, The Matrix.']
        ];

        foreach ($notesData as $data) {
            if (isset($categories[$data['category']])) {
                Note::updateOrCreate(
                    ['title' => $data['title']], // Search condition
                    [
                        'category_id' => $categories[$data['category']]->id,
                        'content' => $data['content']
                    ]
                );
            }
        }
    }
}
