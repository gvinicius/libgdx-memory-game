#!/usr/bin/env python3
"""Generate mockup screenshots of the Stroop Memory Game."""

from PIL import Image, ImageDraw, ImageFont
import math

WIDTH = 1280
HEIGHT = 800
BG_COLOR = (20, 20, 30)

CARD_COLORS = {
    'RED': (255, 60, 60),
    'BLUE': (60, 100, 255),
    'GREEN': (60, 200, 80),
    'YELLOW': (255, 255, 60),
    'PURPLE': (150, 50, 200),
    'ORANGE': (255, 165, 0),
    'PINK': (255, 180, 200),
    'WHITE': (255, 255, 255),
}

CARD_BACK = (50, 50, 75)
CARD_BACK_BORDER = (100, 100, 130)
CARD_BACK_INNER = (65, 65, 100)
MATCHED_BG = (38, 100, 38)


def draw_card_face(draw, cx, cy, radius, color_name, display_color):
    """Draw a face-up Stroop card."""
    draw.ellipse([cx - radius, cy - radius, cx + radius, cy + radius],
                 fill=(38, 38, 50))
    draw.ellipse([cx - radius*0.85, cy - radius*0.85, cx + radius*0.85, cy + radius*0.85],
                 fill=CARD_BACK_BORDER)
    draw.ellipse([cx - radius*0.7, cy - radius*0.7, cx + radius*0.7, cy + radius*0.7],
                 fill=display_color)

    try:
        font = ImageFont.truetype("/usr/share/fonts/truetype/dejavu/DejaVuSans-Bold.ttf", 18)
    except:
        font = ImageFont.load_default()

    bbox = draw.textbbox((0, 0), color_name, font=font)
    tw = bbox[2] - bbox[0]
    th = bbox[3] - bbox[1]
    draw.text((cx - tw // 2, cy - th // 2), color_name, fill=(255, 255, 255), font=font)


def draw_card_back(draw, cx, cy, radius):
    """Draw a face-down card."""
    draw.ellipse([cx - radius, cy - radius, cx + radius, cy + radius],
                 fill=CARD_BACK)
    draw.ellipse([cx - radius*0.85, cy - radius*0.85, cx + radius*0.85, cy + radius*0.85],
                 fill=CARD_BACK_BORDER)
    draw.ellipse([cx - radius*0.7, cy - radius*0.7, cx + radius*0.7, cy + radius*0.7],
                 fill=CARD_BACK_INNER)
    draw.ellipse([cx - radius*0.15, cy - radius*0.15, cx + radius*0.15, cy + radius*0.15],
                 fill=(75, 75, 130))


def draw_matched_card(draw, cx, cy, radius, color_name, display_color):
    """Draw a matched card."""
    draw.ellipse([cx - radius, cy - radius, cx + radius, cy + radius],
                 fill=MATCHED_BG)
    draw.ellipse([cx - radius*0.85, cy - radius*0.85, cx + radius*0.85, cy + radius*0.85],
                 fill=CARD_BACK_BORDER)
    draw.ellipse([cx - radius*0.7, cy - radius*0.7, cx + radius*0.7, cy + radius*0.7],
                 fill=display_color)

    try:
        font = ImageFont.truetype("/usr/share/fonts/truetype/dejavu/DejaVuSans-Bold.ttf", 18)
    except:
        font = ImageFont.load_default()

    bbox = draw.textbbox((0, 0), color_name, font=font)
    tw = bbox[2] - bbox[0]
    th = bbox[3] - bbox[1]
    draw.text((cx - tw // 2, cy - th // 2), color_name, fill=(255, 255, 255), font=font)


def draw_hud(draw, level_str, pairs_matched, total_pairs, lives, errors):
    try:
        font = ImageFont.truetype("/usr/share/fonts/truetype/dejavu/DejaVuSans.ttf", 20)
    except:
        font = ImageFont.load_default()

    hud_text = f"STROOP  Level: {level_str}  Pairs: {pairs_matched}/{total_pairs}  Lives: {lives}  Errors: {errors}"
    draw.text((20, HEIGHT - 50), hud_text, fill=(255, 255, 255), font=font)


def generate_memorization_screenshot():
    """Screenshot 1: Memorization phase - all cards face up."""
    img = Image.new('RGB', (WIDTH, HEIGHT), BG_COLOR)
    draw = ImageDraw.Draw(img)

    try:
        title_font = ImageFont.truetype("/usr/share/fonts/truetype/dejavu/DejaVuSans-Bold.ttf", 28)
    except:
        title_font = ImageFont.load_default()

    draw.text((WIDTH // 2 - 180, 20), "MEMORIZATION PHASE", fill=(255, 255, 100), font=title_font)

    cards = [
        ('RED', CARD_COLORS['BLUE']),
        ('BLUE', CARD_COLORS['GREEN']),
        ('GREEN', CARD_COLORS['RED']),
        ('RED', CARD_COLORS['PURPLE']),
        ('BLUE', CARD_COLORS['ORANGE']),
        ('GREEN', CARD_COLORS['YELLOW']),
    ]

    radius = 60
    cols = 3
    rows = 2
    spacing_x = 200
    spacing_y = 200
    start_x = WIDTH // 2 - (cols - 1) * spacing_x // 2
    start_y = HEIGHT // 2 - (rows - 1) * spacing_y // 2

    for i, (name, color) in enumerate(cards):
        col = i % cols
        row = i // cols
        cx = start_x + col * spacing_x
        cy = start_y + row * spacing_y
        draw_card_face(draw, cx, cy, radius, name, color)

    draw_hud(draw, "1-1", 0, 3, 3, 0)
    img.save('/home/user/libgdx-memory-game/screenshots/01_memorization_phase.png')
    print("Generated: 01_memorization_phase.png")


def generate_playing_screenshot():
    """Screenshot 2: Playing phase - some cards face down, two flipped."""
    img = Image.new('RGB', (WIDTH, HEIGHT), BG_COLOR)
    draw = ImageDraw.Draw(img)

    try:
        title_font = ImageFont.truetype("/usr/share/fonts/truetype/dejavu/DejaVuSans-Bold.ttf", 28)
    except:
        title_font = ImageFont.load_default()

    draw.text((WIDTH // 2 - 120, 20), "PLAYING PHASE", fill=(100, 255, 100), font=title_font)

    radius = 60
    cols = 3
    rows = 2
    spacing_x = 200
    spacing_y = 200
    start_x = WIDTH // 2 - (cols - 1) * spacing_x // 2
    start_y = HEIGHT // 2 - (rows - 1) * spacing_y // 2

    # Card states: face_down, face_up (RED/BLUE), face_down, face_up (RED/PURPLE), face_down, face_down
    for i in range(6):
        col = i % cols
        row = i // cols
        cx = start_x + col * spacing_x
        cy = start_y + row * spacing_y

        if i == 0:
            draw_card_face(draw, cx, cy, radius, 'RED', CARD_COLORS['BLUE'])
        elif i == 3:
            draw_card_face(draw, cx, cy, radius, 'RED', CARD_COLORS['PURPLE'])
        else:
            draw_card_back(draw, cx, cy, radius)

    # Add annotation arrow showing the match
    try:
        annot_font = ImageFont.truetype("/usr/share/fonts/truetype/dejavu/DejaVuSans-Bold.ttf", 16)
    except:
        annot_font = ImageFont.load_default()

    draw.text((WIDTH // 2 - 140, HEIGHT // 2 + 140),
              'Match by COLOR NAME (both say "RED"!)',
              fill=(255, 255, 100), font=annot_font)

    draw_hud(draw, "1-1", 0, 3, 3, 0)
    img.save('/home/user/libgdx-memory-game/screenshots/02_playing_stroop_match.png')
    print("Generated: 02_playing_stroop_match.png")


def generate_partial_progress_screenshot():
    """Screenshot 3: Mid-game with some matched pairs."""
    img = Image.new('RGB', (WIDTH, HEIGHT), BG_COLOR)
    draw = ImageDraw.Draw(img)

    try:
        title_font = ImageFont.truetype("/usr/share/fonts/truetype/dejavu/DejaVuSans-Bold.ttf", 28)
    except:
        title_font = ImageFont.load_default()

    draw.text((WIDTH // 2 - 100, 20), "MID-GAME", fill=(100, 200, 255), font=title_font)

    radius = 60
    cols = 3
    rows = 2
    spacing_x = 200
    spacing_y = 200
    start_x = WIDTH // 2 - (cols - 1) * spacing_x // 2
    start_y = HEIGHT // 2 - (rows - 1) * spacing_y // 2

    cards = [
        ('matched', 'RED', CARD_COLORS['BLUE']),
        ('face_down', 'BLUE', CARD_COLORS['GREEN']),
        ('face_down', 'GREEN', CARD_COLORS['RED']),
        ('matched', 'RED', CARD_COLORS['PURPLE']),
        ('face_up', 'BLUE', CARD_COLORS['ORANGE']),
        ('face_down', 'GREEN', CARD_COLORS['YELLOW']),
    ]

    for i, (state, name, color) in enumerate(cards):
        col = i % cols
        row = i // cols
        cx = start_x + col * spacing_x
        cy = start_y + row * spacing_y

        if state == 'matched':
            draw_matched_card(draw, cx, cy, radius, name, color)
        elif state == 'face_up':
            draw_card_face(draw, cx, cy, radius, name, color)
        else:
            draw_card_back(draw, cx, cy, radius)

    draw_hud(draw, "1-1", 1, 3, 2, 1)
    img.save('/home/user/libgdx-memory-game/screenshots/03_mid_game_progress.png')
    print("Generated: 03_mid_game_progress.png")


def generate_stroop_effect_screenshot():
    """Screenshot 4: Close-up showing the Stroop effect - word says one color, displayed in another."""
    img = Image.new('RGB', (WIDTH, HEIGHT), BG_COLOR)
    draw = ImageDraw.Draw(img)

    try:
        title_font = ImageFont.truetype("/usr/share/fonts/truetype/dejavu/DejaVuSans-Bold.ttf", 32)
        desc_font = ImageFont.truetype("/usr/share/fonts/truetype/dejavu/DejaVuSans.ttf", 18)
    except:
        title_font = ImageFont.load_default()
        desc_font = ImageFont.load_default()

    draw.text((WIDTH // 2 - 220, 30), "STROOP EFFECT DEMO", fill=(255, 200, 100), font=title_font)
    draw.text((WIDTH // 2 - 280, 80),
              'The word and the display color are ALWAYS different!',
              fill=(200, 200, 200), font=desc_font)
    draw.text((WIDTH // 2 - 280, 110),
              'Match cards by the WORD, not the color you see.',
              fill=(200, 200, 200), font=desc_font)

    radius = 80
    examples = [
        ('RED', CARD_COLORS['BLUE'], 'Says "RED" in Blue'),
        ('RED', CARD_COLORS['GREEN'], 'Says "RED" in Green'),
        ('BLUE', CARD_COLORS['YELLOW'], 'Says "BLUE" in Yellow'),
        ('BLUE', CARD_COLORS['PINK'], 'Says "BLUE" in Pink'),
    ]

    start_y = 280
    spacing_x = 280
    start_x = WIDTH // 2 - (len(examples) - 1) * spacing_x // 2

    for i, (name, color, desc) in enumerate(examples):
        cx = start_x + i * spacing_x
        cy = start_y
        draw_card_face(draw, cx, cy, radius, name, color)

        bbox = draw.textbbox((0, 0), desc, font=desc_font)
        tw = bbox[2] - bbox[0]
        draw.text((cx - tw // 2, cy + radius + 20), desc, fill=(180, 180, 180), font=desc_font)

    # Draw match arrows
    arrow_y = start_y + radius + 60
    draw.text((start_x + spacing_x // 2 - 30, arrow_y + 30), "MATCH!", fill=(100, 255, 100), font=title_font)
    draw.text((start_x + spacing_x * 2 + spacing_x // 2 - 30, arrow_y + 30), "MATCH!", fill=(100, 255, 100), font=title_font)

    # Arrows connecting pairs
    draw.line([(start_x, arrow_y), (start_x + spacing_x, arrow_y)], fill=(100, 255, 100), width=3)
    draw.line([(start_x + spacing_x * 2, arrow_y), (start_x + spacing_x * 3, arrow_y)], fill=(100, 255, 100), width=3)

    draw.text((WIDTH // 2 - 320, HEIGHT - 140),
              'Different display colors, same word = they match!',
              fill=(255, 255, 100), font=desc_font)
    draw.text((WIDTH // 2 - 320, HEIGHT - 110),
              'This trains your brain to read the word, not react to the color.',
              fill=(200, 200, 200), font=desc_font)

    img.save('/home/user/libgdx-memory-game/screenshots/04_stroop_effect_explained.png')
    print("Generated: 04_stroop_effect_explained.png")


if __name__ == '__main__':
    generate_memorization_screenshot()
    generate_playing_screenshot()
    generate_partial_progress_screenshot()
    generate_stroop_effect_screenshot()
    print("All screenshots generated successfully!")
