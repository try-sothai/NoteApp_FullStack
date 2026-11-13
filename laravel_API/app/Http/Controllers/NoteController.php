<?php

namespace App\Http\Controllers;

use App\Models\Note;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\Validator;

class NoteController extends Controller
{
    public function index()
    {
        $notes = Note::with('category')->get();
        return response()->json([
            'success' => true,
            'message' => 'Notes retrieved successfully',
            'data' => $notes
        ], 200);
    }

    public function show($id)
    {
        $note = Note::with('category')->find($id);

        if (!$note) {
            return response()->json([
                'success' => false,
                'message' => 'Note not found',
                'data' => null
            ], 404);
        }

        return response()->json([
            'success' => true,
            'message' => 'Note retrieved successfully',
            'data' => $note
        ], 200);
    }

    public function store(Request $request)
    {
        $validator = Validator::make($request->all(), [
            'category_id' => 'required|exists:categories,id',
            'title' => 'required|string|max:255',
            'content' => 'nullable|string'
        ]);

        if ($validator->fails()) {
            return response()->json([
                'success' => false,
                'message' => 'Validation failed',
                'errors' => $validator->errors()
            ], 422);
        }

        $note = Note::create($validator->validated());
        $note->load('category');

        return response()->json([
            'success' => true,
            'message' => 'Note created successfully',
            'data' => $note
        ], 201);
    }

    public function update(Request $request, $id)
    {
        $note = Note::find($id);

        if (!$note) {
            return response()->json([
                'success' => false,
                'message' => 'Note not found',
                'data' => null
            ], 404);
        }

        $validator = Validator::make($request->all(), [
            'category_id' => 'required|exists:categories,id',
            'title' => 'required|string|max:255',
            'content' => 'nullable|string'
        ]);

        if ($validator->fails()) {
            return response()->json([
                'success' => false,
                'message' => 'Validation failed',
                'errors' => $validator->errors()
            ], 422);
        }

        $note->update($validator->validated());
        $note->load('category');

        return response()->json([
            'success' => true,
            'message' => 'Note updated successfully',
            'data' => $note
        ], 200);
    }

    public function destroy($id)
    {
        $note = Note::find($id);

        if (!$note) {
            return response()->json([
                'success' => false,
                'message' => 'Note not found',
                'data' => null
            ], 404);
        }

        $note->delete();

        return response()->json([
            'success' => true,
            'message' => 'Note deleted successfully',
            'data' => null
        ], 200);
    }
}
