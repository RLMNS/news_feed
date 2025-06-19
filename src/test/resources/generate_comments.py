import csv
from datetime import datetime, timedelta
import random

def random_datetime(start_year, end_year):
  year = random.randint(start_year, end_year)
  month = random.randint(1, 12)
  day = random.randint(1, 28)
  hour = random.randint(0, 23)
  minute = random.randint(0, 59)
  second = random.randint(0, 59)
  return datetime(year, month, day, hour, minute, second)

def generate_comments():
    with open('comments_data.csv', 'w', newline='') as file:
        writer = csv.writer(file)
        writer.writerow(['text', 'created_at', 'username', 'news_id'])

        for i in range(1, 1000001):
            news_id = random.randint(1, 1000)
            created_at = random_datetime(2020, 2025)
            writer.writerow([
                f'Test Comment {i}',
                created_at.strftime('%Y-%m-%d %H:%M:%S'),
                f'user{i % 10000}',
                news_id
            ])

if __name__ == '__main__':
    generate_comments() 